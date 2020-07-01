package ru.etysoft.aurorauniverse;

import org.bukkit.Bukkit;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.ColorCodes;

public class Logger {

    public static void info(String s)
    {
        Bukkit.getConsoleSender().sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + " " + s));
    }

    public static void debug(String s)
    {
        if (AuroraConfiguration.getDebugMode()) {
            Bukkit.getConsoleSender().sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + " &a[DEBUG] &f" + s));
        }
    }

    public static void warning(String s)
    {
        Bukkit.getConsoleSender().sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + "&e " + s));
    }

    public static void error(String s)
    {
        Bukkit.getConsoleSender().sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + "&c " + s));
    }

    public static void fatalError(String s)
    {
        Bukkit.getConsoleSender().sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + "&c[FATAL ERROR] " + s));
    }

}
