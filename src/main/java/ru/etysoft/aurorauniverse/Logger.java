package ru.etysoft.aurorauniverse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {

    public static void info(String s)
    {
        Bukkit.getConsoleSender().sendMessage(fun.cstring(AuroraUniverse.prefix + " " + s));
    }

    public static void warning(String s)
    {
        Bukkit.getConsoleSender().sendMessage(fun.cstring(AuroraUniverse.prefix + "&e " + s));
    }

}
