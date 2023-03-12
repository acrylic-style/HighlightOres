package xyz.acrylicstyle.highlightOres;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityFallingBlock;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import util.CollectionList;
import util.ICollectionList;
import util.promise.Promise;
import xyz.acrylicstyle.highlightOres.gui.OreSelectGui;
import xyz.acrylicstyle.highlightOres.util.BlockDataCache;
import xyz.acrylicstyle.highlightOres.util.Log;
import xyz.acrylicstyle.highlightOres.util.NotTomeitoLib;
import xyz.acrylicstyle.highlightOres.util.Pair;
import xyz.acrylicstyle.highlightOres.util.PairCache;
import xyz.acrylicstyle.highlightOres.util.UUIDMap;

import java.util.AbstractMap;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

public class HighlightOres extends JavaPlugin implements Listener {
    private static final Object writeLock = new Object();
    public static final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(25);
    public static final ThreadPoolExecutor writePool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    public static HighlightOres instance;
    public static final CollectionList<UUID> highlight = new CollectionList<>();
    public static final CollectionList<UUID> pending = new CollectionList<>();
    public static final BlockDataCache cache = new BlockDataCache();
    public static final CollectionList<Float> times = new CollectionList<>();
    public static float minTime = -1;
    public static float maxTime = 0;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        NotTomeitoLib.registerCommands(this.getClassLoader(), "highlight", "xyz.acrylicstyle.highlightOres.commands");
        Objects.requireNonNull(Bukkit.getPluginCommand("highlight")).setTabCompleter(new HighlightTabCompleter());
    }

    @Override
    public void onDisable() {
        entities.keysList().forEach(HighlightOres::clearEntities);
        pool.shutdownNow();
        writePool.shutdownNow();
    }

    public static final UUIDMap<CollectionList<EntityData>> entities = new UUIDMap<>(uuid -> new CollectionList<>());

    public static void clearEntities(UUID uuid) {
        if (!entities.containsKey(uuid)) entities.add(uuid, new CollectionList<>());
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            entities.get(uuid).clear();
            return;
        }
        AtomicReference<Integer[]> arr = new AtomicReference<>();
        arr.set(entities.get(uuid).clone().nonNull().map(EntityData::getEntity).map(Entity::getId).toArray(new Integer[0]));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(ArrayUtils.toPrimitive(arr.get())));
        entities.get(uuid).nonNull().forEach(ed -> player.sendBlockChange(new Location(ed.getEntity().getWorld().getWorld(), ed.getX()-0.5, ed.getY(), ed.getZ()-0.5), ed.getData()));
        entities.get(uuid).clear();
    }

    public static void hideFarEntities(UUID uuid) {
        if (!entities.containsKey(uuid)) entities.add(uuid, new CollectionList<>());
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            entities.get(uuid).clear();
            return;
        }
        if (entities.get(uuid).isEmpty()) return; // don't waste performance
        AtomicReference<Integer[]> arr = new AtomicReference<>();
        ICollectionList<EntityData> farEntities = entities.get(uuid)
                .clone()
                .nonNull()
                .filter(e -> e.getEntity().getBukkitEntity().getLocation().distance(player.getLocation()) > 30);
        arr.set(farEntities.clone().map(EntityData::getEntity).map(Entity::getId).toArray(new Integer[0]));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(ArrayUtils.toPrimitive(arr.get())));
        farEntities.clone().forEach(ed -> {
            player.sendBlockChange(new Location(ed.getEntity().getWorld().getWorld(), ed.getX() - 0.5, ed.getY(), ed.getZ() - 0.5), ed.getData());
            entities.get(uuid).remove(ed);
        });
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.SPECTATOR) return;
        if (!highlight.contains(e.getPlayer().getUniqueId())) return;
        OreSelectGui gui = OreSelectGui.buildGui(e.getPlayer().getUniqueId());
        if (gui.getSelectedMaterials().size() == 0) return;
        if (!entities.containsKey(e.getPlayer().getUniqueId())) entities.add(e.getPlayer().getUniqueId(), new CollectionList<>()); // create list if not exists
        if (pending.contains(e.getPlayer().getUniqueId())) return;
        render(e.getPlayer(), gui);
    }

    public static void render(Player player, OreSelectGui gui) {
        pending.add(player.getUniqueId());
        new Promise<Object>() {
            @Override
            public Object apply(Object o) {
                pool.execute(() -> {
                    hideFarEntities(player.getUniqueId());
                    List<Map.Entry<Location, Material>> blocks = getNearbyBlocks(player.getLocation(), 15, gui.getSelectedMaterials());
                    if (!highlight.contains(player.getUniqueId())) {
                        pending.remove(player.getUniqueId());
                        return;
                    }
                    PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
                    WorldServer s = ((CraftWorld) player.getWorld()).getHandle();
                    blocks.forEach(block -> {
                        player.sendBlockChange(block.getKey(), Material.AIR.createBlockData());
                        CraftBlockData data = cache.getOrCreate(block.getValue());
                        EntityFallingBlock entity = new EntityFallingBlock(s, block.getKey().getX() + 0.5, block.getKey().getY(), block.getKey().getZ() + 0.5, data.getState());
                        EntityData ed = new EntityData(entity, data);
                        try {
                            if (entities.get(player.getUniqueId()).nonNull().filter(ed::equals).size() > 0) return;
                        } catch (ConcurrentModificationException ignored) {
                        }
                        entity.setLocation(block.getKey().getX() + 0.5, block.getKey().getY(), block.getKey().getZ() + 0.5, 0, 0);
                        entity.setNoGravity(true);
                        entity.glowing = true;
                        entity.setFlag(6, true);
                        entities.get(player.getUniqueId()).add(ed);
                        pc.sendPacket(new PacketPlayOutSpawnEntity(entity, net.minecraft.server.v1_15_R1.Block.getCombinedId(data.getState())));
                        pc.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true));
                    });
                    if (!highlight.contains(player.getUniqueId())) clearEntities(player.getUniqueId());
                    pending.remove(player.getUniqueId());
                    resolve(null);
                });
                return waitUntilResolve(1000 * 10);
            }
        }.then(o -> pending.remove(player.getUniqueId())).queue();
    }

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(instance, runnable);
    }

    @NotNull
    public static List<Map.Entry<Location, Material>> getNearbyBlocks(@NotNull Location location, int radius, List<Material> materials) {
        CollectionList<BlockPosition> locations = new CollectionList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    if (y >= 0) {
                        locations.add(new BlockPosition(x, y, z));
                    }
                }
            }
        }
        long start = System.currentTimeMillis();
        CraftWorld world = ((CraftWorld) location.getWorld());
        assert world != null;
        List<Map.Entry<Location, Material>> loc1 = locations
                .map(loc -> new AbstractMap.SimpleEntry<>(getBlockData(world, loc), loc))
                .filter(data -> materials.contains(data.getKey().getMaterial()))
                .map(loc -> new AbstractMap.SimpleEntry<>(new Location(world, loc.getValue().getX(), loc.getValue().getY(), loc.getValue().getZ()), loc.getKey().getMaterial()));

        // record time
        long end = System.currentTimeMillis();
        float time = (end - start) / 1000F;
        writePool.execute(() -> {
            synchronized (writeLock) {
                times.add(0, time);
                if (times.size() > 100) {
                    for (int i = 100; i < times.size(); i++) {
                        times.remove(i - 1);
                    }
                }
                times.removeIf(Objects::isNull);
            }
        });
        if (maxTime < time) {
            maxTime = time;
        }
        if (minTime < 0 || minTime > time) {
            minTime = time;
        }
        return loc1;
    }

    public static double getAverageTime() {
        return Math.round(times.reduce(ICollectionList.Reducer.SUM_FLOAT, 0F) / (float) times.size() * 100F) / 100D;
    }

    private static final PairCache blockDataCache = new PairCache();

    public static CraftBlockData getBlockData(World w, BlockPosition loc) {
        boolean clearedCache = false;
        if (blockDataCache.size() > 500000) {
            blockDataCache.clear();
            clearedCache = true;
        }
        Pair<Integer, Integer, Integer> x = new Pair<>(loc.getX(), loc.getY(), loc.getZ());
        if (!blockDataCache.containsKey(x) || blockDataCache.get(x) == null) {
            long start = System.currentTimeMillis();
            CraftBlockData data = (CraftBlockData) w.getBlockAt(loc.getX(), loc.getY(), loc.getZ()).getBlockData();
            long end = System.currentTimeMillis();
            if (end - start > 1000) {
                Log.warn("Took " + (end-start) + "ms to get block at " + loc);
                Log.warn("block was: " + data.getMaterial().name());
                Log.warn("Cache size: " + blockDataCache.size());
                Log.warn("Cleared cache: " + clearedCache);
            }
            blockDataCache.put(x, data);
            return data;
        }
        return blockDataCache.get(x);
    }
}
