package xyz.acrylicstyle.highlightOres.commands;

import org.bukkit.entity.Player;
import xyz.acrylicstyle.highlightOres.gui.OreSelectGui;
import xyz.acrylicstyle.highlightOres.subcommand.PlayerSubCommandExecutor;
import xyz.acrylicstyle.highlightOres.subcommand.SubCommand;

@SubCommand(name = "select", usage = "/highlight select", description = "Selects the highlighting ores.")
public class SelectCommand extends PlayerSubCommandExecutor {
    @Override
    public void onCommand(Player player, String[] args) {
        player.openInventory(OreSelectGui.buildGui(player.getUniqueId()).getInventory());
    }
}
