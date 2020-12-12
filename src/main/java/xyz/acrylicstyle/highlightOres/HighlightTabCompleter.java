package xyz.acrylicstyle.highlightOres;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.acrylicstyle.tomeito_api.utils.TabCompleterHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HighlightTabCompleter extends TabCompleterHelper implements TabCompleter {
    private static final List<String> emptyList = Collections.emptyList();
    private static final List<String> commands = Arrays.asList("toggle", "select");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) return commands;
        if (args.length == 1) return filterArgsList(commands, args[0]);
        return emptyList;
    }
}
