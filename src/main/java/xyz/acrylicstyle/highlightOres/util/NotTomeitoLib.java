package xyz.acrylicstyle.highlightOres.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.CollectionList;
import util.ICollectionList;
import util.ReflectionHelper;
import util.StringCollection;
import xyz.acrylicstyle.highlightOres.subcommand.SubCommand;
import xyz.acrylicstyle.highlightOres.subcommand.SubCommandExecutor;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotTomeitoLib {
    /**
     * Ensure the {@link CommandSender} is a {@link Player}.
     * @return {@link Player} if {@link CommandSender} was player, null otherwise.
     * You need to run only <pre>
     *     Player player = TomeitoAPI.ensurePlayer(sender);<br />
     *     if (player == null) return;<br />
     *     // your code
     * </pre> as this method sends message to the sender when does not meet requirements.
     */
    @Nullable
    public static Player ensurePlayer(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be used from console.");
            return null;
        }
        return (Player) sender;
    }

    public static void registerCommands(@NotNull ClassLoader classLoader, @NotNull final String rootCommandName, @NotNull final String subCommandsPackage) {
        registerCommands(classLoader, rootCommandName, subCommandsPackage, (sender, command, label, args) -> true);
    }

    private final static StringCollection<List<Map.Entry<SubCommand, SubCommandExecutor>>> subCommands = new StringCollection<>();

    /**
     * Registers command with sub commands.
     * @param classLoader Class loader that will be used to load classes. System class loader will be used if left null. (which will be unable to load classes under plugin)
     * @param rootCommandName A root command name. Must be defined at plugin.yml.
     * @param subCommandsPackage Package name that contains sub commands classes. Must be annotated with SubCommand and must extend SubCommandExecutor.
     * @param postCommand A CommandExecutor that runs very first. Return false to interrupt command execution.
     */
    public static void registerCommands(@Nullable ClassLoader classLoader, @NotNull final String rootCommandName, @NotNull final String subCommandsPackage, @Nullable CommandExecutor postCommand) {
        if (classLoader == null) {
            classLoader = NotTomeitoLib.class.getClassLoader();
            Log.info("Using default class loader to register sub commands on " + subCommandsPackage);
        }
        CollectionList<Class<?>> classes = ReflectionHelper.findAllAnnotatedClasses(classLoader, subCommandsPackage, SubCommand.class);
        //Log.info("Found " + classes.size() + " classes under " + subCommandsPackage + "(/" + rootCommandName + ")");
        registerCommands(rootCommandName, classes, postCommand);
    }

    /**
     * Registers command with sub commands.
     * @param rootCommandName A root command name. Must be defined at plugin.yml.
     * @param classes Class list that will load. All classes must implement SubCommandExecutor or it will fail to load.
     * @param postCommand A CommandExecutor that runs very first. Return false to interrupt command execution.
     */
    public static void registerCommands(@NotNull final String rootCommandName, @NotNull final CollectionList<Class<?>> classes, @Nullable CommandExecutor postCommand) {
        final CollectionList<Map.Entry<SubCommand, SubCommandExecutor>> commands = new CollectionList<>();
        classes.forEach(clazz -> {
            SubCommand command = clazz.getAnnotation(SubCommand.class);
            try {
                SubCommandExecutor subCommandExecutor = (SubCommandExecutor) clazz.newInstance();
                commands.add(new AbstractMap.SimpleImmutableEntry<>(command, subCommandExecutor));
                //Log.debug("Registered sub command at " + rootCommandName + ": " + command.name());
                subCommands.add(rootCommandName, commands);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                Log.warning("Couldn't cast class to SubCommandExecutor!");
                Log.warning("Class: " + clazz.getCanonicalName());
                Log.warning("Make sure this class implements SubCommandExecutor, then try again.");
            }
        });
        subCommands.forEach((s, l) -> Log.debug("Command " + s + " has " + l.size() + " sub commands"));
        @NotNull CommandExecutor finalPostCommand = postCommand == null ? ((sender, command, label, args) -> true) : postCommand;
        Objects.requireNonNull(Bukkit.getPluginCommand(rootCommandName)).setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String[] args) {
                if (!finalPostCommand.onCommand(sender, command, label, args)) return true;
                if (args.length == 0) {
                    $sendMessage(sender);
                    return true;
                }
                List<Map.Entry<SubCommand, SubCommandExecutor>> commands = subCommands.get(rootCommandName);
                if (commands == null) throw new IllegalStateException("Root command isn't defined! (Tried to get " + rootCommandName + ")");
                List<Map.Entry<SubCommand, SubCommandExecutor>> entries = ICollectionList.asList(commands).filter(e -> e.getKey().name().equals(args[0]) || e.getKey().alias().equals(args[0]));
                if (entries.size() == 0) {
                    $sendMessage(sender);
                    return true;
                }
                ICollectionList<String> argsList = ICollectionList.asList(args);
                argsList.shift();
                ICollectionList.asList(entries).map(Map.Entry::getValue).forEach(s -> s.onCommand(sender, argsList.toArray(new String[0])));
                return true;
            }

            @NotNull
            @Contract(pure = true)
            public String getCommandHelp(String command, String description) {
                return ChatColor.YELLOW + command + ChatColor.GRAY + " - " + ChatColor.AQUA + description;
            }

            public void $sendMessage(@NotNull CommandSender sender) {
                sender.sendMessage(ChatColor.GOLD + "-----------------------------------");
                List<Map.Entry<SubCommand, SubCommandExecutor>> commands = subCommands.get(rootCommandName);
                ICollectionList.asList(commands).map(Map.Entry::getKey).sorted(Comparator.comparing(SubCommand::name)).forEach(s -> sender.sendMessage(getCommandHelp(s.usage(), s.description())));
                sender.sendMessage(ChatColor.GOLD + "-----------------------------------");
            }
        });
    }
}
