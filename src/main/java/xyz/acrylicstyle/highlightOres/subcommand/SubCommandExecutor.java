package xyz.acrylicstyle.highlightOres.subcommand;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface SubCommandExecutor {
    void onCommand(@NotNull CommandSender sender, @NotNull String[] args);
}
