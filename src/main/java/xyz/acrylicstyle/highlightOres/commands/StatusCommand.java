package xyz.acrylicstyle.highlightOres.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.highlightOres.HighlightOres;
import xyz.acrylicstyle.highlightOres.subcommand.SubCommand;
import xyz.acrylicstyle.highlightOres.subcommand.SubCommandExecutor;

@SubCommand(name = "status", usage = "/highlight status", description = "Shows current status of plugin.")
public class StatusCommand implements SubCommandExecutor {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Active tasks: " + ChatColor.RED + HighlightOres.writePool.getActiveCount());
        sender.sendMessage(ChatColor.YELLOW + "Tasks in queue: " + ChatColor.RED + HighlightOres.writePool.getQueue().size());
        sender.sendMessage(ChatColor.YELLOW + "Tasks in pool: " + ChatColor.RED + HighlightOres.writePool.getPoolSize());
        sender.sendMessage(ChatColor.YELLOW + "Max pool size: " + ChatColor.RED + HighlightOres.writePool.getMaximumPoolSize());
        sender.sendMessage(ChatColor.YELLOW + "Largest pool size: " + ChatColor.RED + HighlightOres.writePool.getLargestPoolSize());
        sender.sendMessage(ChatColor.YELLOW + "Core pool size: " + ChatColor.RED + HighlightOres.writePool.getCorePoolSize());
        sender.sendMessage(ChatColor.YELLOW + "Shortest render time: " + ChatColor.RED + HighlightOres.minTime + "s");
        sender.sendMessage(ChatColor.YELLOW + "Average render time past 100 times: " + ChatColor.RED + HighlightOres.getAverageTime() + "s");
        sender.sendMessage(ChatColor.YELLOW + "Longest render time: " + ChatColor.RED + HighlightOres.maxTime + "s");
    }
}
