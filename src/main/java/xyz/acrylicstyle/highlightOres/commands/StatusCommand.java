package xyz.acrylicstyle.highlightOres.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.highlightOres.HighlightOres;
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommand;
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommandExecutor;

@SubCommand(name = "status", usage = "/highlight status", description = "Shows current status of plugin.")
public class StatusCommand implements SubCommandExecutor {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Active tasks: " + ChatColor.RED + HighlightOres.pool.getActiveCount());
        sender.sendMessage(ChatColor.YELLOW + "Tasks in queue: " + ChatColor.RED + HighlightOres.pool.getQueue().size());
        sender.sendMessage(ChatColor.YELLOW + "Tasks in pool: " + ChatColor.RED + HighlightOres.pool.getPoolSize());
        sender.sendMessage(ChatColor.YELLOW + "Max pool size: " + ChatColor.RED + HighlightOres.pool.getMaximumPoolSize());
        sender.sendMessage(ChatColor.YELLOW + "Largest pool size: " + ChatColor.RED + HighlightOres.pool.getLargestPoolSize());
        sender.sendMessage(ChatColor.YELLOW + "Core pool size: " + ChatColor.RED + HighlightOres.pool.getCorePoolSize());
    }
}
