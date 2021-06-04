package xyz.acrylicstyle.highlightOres.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import util.Validate;

public class Log {
    @NotNull
    public static java.util.logging.Logger as(String title) { return java.util.logging.Logger.getLogger(title); }

    public static void info(String name, String msg) { Bukkit.getLogger().info("[" + name + "] " + msg); }
    public static void warning(String name, String msg) { Bukkit.getLogger().warning("[" + name + "] " + msg); }
    public static void severe(String name, String msg) { Bukkit.getLogger().severe("[" + name + "] " + msg); }
    public static void config(String name, String msg) { Bukkit.getLogger().config("[" + name + "] " + msg); }
    public static void fine(String name, String msg) { Bukkit.getLogger().fine("[" + name + "] " + msg); }
    public static void finer(String name, String msg) { Bukkit.getLogger().finer("[" + name + "] " + msg); }
    public static void finest(String name, String msg) { Bukkit.getLogger().finest("[" + name + "] " + msg); }
    public static void debug(String name, String msg) { Bukkit.getLogger().info("[" + name + "] [DEBUG] " + msg); }

    public static void info(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            info(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void warning(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            warning(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void warn(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            warning(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void severe(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            severe(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void error(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            severe(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void config(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            config(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void fine(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            fine(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void finer(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            finer(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void finest(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            finest(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void debug(String msg) {
        try {
            String name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
            if (name.equalsIgnoreCase("")) name = "Anonymous";
            debug(name, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public static Logger of(String title) { return new Logger(title); }

    @NotNull
    public static Logger with(String title) { return new Logger(title); }

    @NotNull
    public static Logger getLogger(String title) { return new Logger(title); }

    @NotNull
    public static Logger getLogger() {
        String name;
        try {
            name = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (name.equalsIgnoreCase("")) name = "Anonymous";
        return new Logger(name);
    }

    public static final class Logger {
        private final String name;

        private Logger(String name) {
            Validate.notNull(name, "title cannot be null");
            this.name = name;
        }

        public void info(String msg) { Log.info(name, msg); }
        public void warning(String msg) { Log.warning(name, msg); }
        public void warn(String msg) { Log.warning(name, msg); }
        public void severe(String msg) { Log.severe(name, msg); }
        public void error(String msg) { Log.severe(name, msg); }
        public void config(String msg) { Log.config(name, msg); }
        public void fine(String msg) { Log.fine(name, msg); }
        public void finer(String msg) { Log.finer(name, msg); }
        public void finest(String msg) { Log.finest(name, msg); }
        public void debug(String msg) { Log.debug(name, msg); }
    }
}
