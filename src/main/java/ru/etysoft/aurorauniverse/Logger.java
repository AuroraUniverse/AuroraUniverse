package ru.etysoft.aurorauniverse;

import org.bukkit.Bukkit;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.ColorCodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

    public static void info(String s)
    {
        Bukkit.getConsoleSender().sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + " " + s));
    }

    public static void log(String s)
    {
           writeToFile(s);
    }

    private static final String newLine = System.getProperty("line.separator");
    private static void writeToFile(String msg)  {
        String fileName = "plugins/AuroraUniverse/log.txt";
        PrintWriter printWriter = null;
        File file = new File(fileName);
        try {
            if (!file.exists()) file.createNewFile();
            printWriter = new PrintWriter(new FileOutputStream(fileName, true));
            printWriter.write(newLine + msg);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

    public static void debug(String s)
    {
        if (AuroraLanguage.getDebugMode()) {
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
