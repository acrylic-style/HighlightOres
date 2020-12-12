package xyz.acrylicstyle.highlightOres.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.highlightOres.HighlightOres;
import xyz.acrylicstyle.tomeito_api.gui.PlayerGui;
import xyz.acrylicstyle.tomeito_api.sounds.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static xyz.acrylicstyle.highlightOres.HighlightOres.run;

public class OreSelectGui implements PlayerGui {
    // static stuff //
    private static final Map<UUID, OreSelectGui> cache = new HashMap<>();

    public static OreSelectGui buildGui(UUID uuid) {
        if (!cache.containsKey(uuid)) cache.put(uuid, new OreSelectGui());
        return cache.get(uuid);
    }

    private static ItemStack getItemStack(Material material, String displayName, boolean enchanted) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        if (enchanted) {
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }

    // non-static stuff //

    public OreSelectGui() {
        selectedMaterials.add(Material.DIAMOND_ORE);
        Bukkit.getPluginManager().registerEvents(this, HighlightOres.instance);
    }

    private final List<Material> selectedMaterials = new ArrayList<>();

    public List<Material> getSelectedMaterials() {
        return new ArrayList<>(selectedMaterials);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().getHolder() != this) return;
        e.setCancelled(true);
    }

    public Inventory setItems(Inventory inventory) {
        inventory.setItem(0, getItemStack(Material.DIAMOND_ORE, ChatColor.AQUA + "Diamond Ore", selectedMaterials.contains(Material.DIAMOND_ORE)));
        inventory.setItem(1, getItemStack(Material.EMERALD_ORE, ChatColor.GREEN + "Emerald Ore", selectedMaterials.contains(Material.EMERALD_ORE)));
        inventory.setItem(2, getItemStack(Material.REDSTONE_ORE, ChatColor.RED + "Redstone Ore", selectedMaterials.contains(Material.REDSTONE_ORE)));
        inventory.setItem(3, getItemStack(Material.IRON_ORE, ChatColor.WHITE + "Iron Ore", selectedMaterials.contains(Material.IRON_ORE)));
        inventory.setItem(4, getItemStack(Material.GOLD_ORE, ChatColor.GOLD + "Gold Ore", selectedMaterials.contains(Material.GOLD_ORE)));
        inventory.setItem(5, getItemStack(Material.LAPIS_ORE, ChatColor.BLUE + "Lapis Ore", selectedMaterials.contains(Material.LAPIS_ORE)));
        inventory.setItem(6, getItemStack(Material.COAL_ORE, ChatColor.GRAY + "Coal Ore", selectedMaterials.contains(Material.COAL_ORE)));
        inventory.setItem(7, getItemStack(Material.NETHER_QUARTZ_ORE, ChatColor.WHITE + "Nether Quartz Ore", selectedMaterials.contains(Material.NETHER_QUARTZ_ORE)));
        inventory.setItem(17, getItemStack(Material.BARRIER, ChatColor.RED + "Close", false));
        return inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return setItems(Bukkit.createInventory(this, 18, "Select the ore"));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() != this) return;
        if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
            e.setCancelled(true);
            return;
        }
        if (e.getClickedInventory() == null || e.getClickedInventory().getHolder() != this) return;
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (e.getSlot() == 0) {
            check(p, e.getInventory(), Material.DIAMOND_ORE);
        } else if (e.getSlot() == 1) {
            check(p, e.getInventory(), Material.EMERALD_ORE);
        } else if (e.getSlot() == 2) {
            check(p, e.getInventory(), Material.REDSTONE_ORE);
        } else if (e.getSlot() == 3) {
            check(p, e.getInventory(), Material.IRON_ORE);
        } else if (e.getSlot() == 4) {
            check(p, e.getInventory(), Material.GOLD_ORE);
        } else if (e.getSlot() == 5) {
            check(p, e.getInventory(), Material.LAPIS_ORE);
        } else if (e.getSlot() == 6) {
            check(p, e.getInventory(), Material.COAL_ORE);
        } else if (e.getSlot() == 7) {
            check(p, e.getInventory(), Material.NETHER_QUARTZ_ORE);
        } else if (e.getSlot() == 17) {
            p.closeInventory();
        }
    }

    private void check(@NotNull Player player, @NotNull Inventory inventory, @NotNull Material material) {
        if (selectedMaterials.contains(material)) {
            selectedMaterials.remove(material);
        } else {
            selectedMaterials.add(material);
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 2);
        HighlightOres.clearEntities(player.getUniqueId());
        HighlightOres.render(player, this);
        run(() -> player.openInventory(setItems(inventory)));
    }
}
