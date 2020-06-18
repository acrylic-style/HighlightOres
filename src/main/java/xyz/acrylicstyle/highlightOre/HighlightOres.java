package xyz.acrylicstyle.highlightOre;

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
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import util.Collection;
import util.CollectionList;
import xyz.acrylicstyle.highlightOre.gui.OreSelectGui;
import xyz.acrylicstyle.highlightOre.util.BlockDataCache;
import xyz.acrylicstyle.tomeito_api.TomeitoAPI;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class HighlightOres extends JavaPlugin implements Listener {
    public static HighlightOres instance;
    public static final CollectionList<UUID> highlight = new CollectionList<>();
    public static final CollectionList<UUID> pending = new CollectionList<>();
    public static final BlockDataCache cache = new BlockDataCache();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        TomeitoAPI.getInstance().registerCommands(this.getClassLoader(), "highlight", "xyz.acrylicstyle.highlightOre.commands");
    }

    public static final Collection<UUID, CollectionList<EntityData>> entities = new Collection<>();

    public static void clearEntities(UUID uuid) {
        if (!entities.containsKey(uuid)) entities.add(uuid, new CollectionList<>());
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            entities.get(uuid).clear();
            return;
        }
        AtomicReference<Integer[]> arr = new AtomicReference<>();
        arr.set(entities.get(uuid).clone().map(EntityData::getEntity).map(Entity::getId).toArray(new Integer[0]));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(ArrayUtils.toPrimitive(arr.get())));
        entities.get(uuid).forEach(ed -> player.sendBlockChange(new Location(ed.getEntity().getWorld().getWorld(), ed.getX()-0.5, ed.getY(), ed.getZ()-0.5), ed.getData()));
        entities.get(uuid).clear();
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
        new Thread(() -> {
            CollectionList<Block> blocks = getNearbyBlocks(player.getLocation(), 25, gui.getSelectedMaterials());
            if (!highlight.contains(player.getUniqueId())) {
                pending.remove(player.getUniqueId());
                return;
            }
            PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
            WorldServer s = ((CraftWorld) player.getWorld()).getHandle();
            blocks.forEach(block -> {
                CraftBlockData data = cache.getOrCreate(block.getType());
                EntityFallingBlock entity = new EntityFallingBlock(s, block.getX()+0.5, block.getY(), block.getZ()+0.5, data.getState());
                EntityData ed = new EntityData(entity, data);
                if (entities.get(player.getUniqueId()).filter(fb -> fb.equals(ed)).size() > 0) return;
                entity.setLocation(block.getX()+0.5, block.getY(), block.getZ()+0.5, 0, 0);
                entity.setNoGravity(true);
                entity.glowing = true;
                entity.setFlag(6, true);
                entities.get(player.getUniqueId()).add(ed);
                pc.sendPacket(new PacketPlayOutSpawnEntity(entity, net.minecraft.server.v1_15_R1.Block.getCombinedId(data.getState())));
                pc.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true));
            });
            if (!highlight.contains(player.getUniqueId())) clearEntities(player.getUniqueId());
            pending.remove(player.getUniqueId());
        }).start();
    }

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(instance, runnable);
    }

    @NotNull
    public static CollectionList<Block> getNearbyBlocks(@NotNull Location location, int radius, List<Material> materials) {
        CollectionList<Block> blocks = new CollectionList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (materials.contains(block.getType())) blocks.add(block);
                }
            }
        }
        return blocks;
    }
}
