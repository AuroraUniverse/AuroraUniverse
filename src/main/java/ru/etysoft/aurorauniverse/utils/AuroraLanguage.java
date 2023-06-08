package ru.etysoft.aurorauniverse.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AuroraLanguage {

    public static String getColorString(String path)
    {
        try {
            if (AuroraUniverse.getLanguage().get(path) != null) {
                return ColorCodes.toColor(AuroraUniverse.getLanguage().getString(path));
            } else {
                Logger.warning("Can't get string " + path + " in language file, trying default value!");
                try {
                    String result = AuroraUniverse.getInstance().getConfig().getDefaults().getString(path);
                    if (result != null) {

                        return result;
                    } else {
                        Logger.error("Can't find default string with path " + path);
                        return ColorCodes.toColor("&cWrong path to String!");
                    }
                } catch (Exception e) {
                    Logger.error("Can't find default string with path " + path + " (NullPointerException)");
                    return ColorCodes.toColor("&cWrong path to translation (" + path + ")");
                }
            }
        }
        catch (Exception e)
        {
            return ColorCodes.toColor("&cConfig string error!");
        }
    }


    public static void setDebugMode(boolean isdebug)
    {
        AuroraUniverse.getInstance().getConfig().set("debug-mode", isdebug);
    }


    public static boolean getDebugMode()
    {
      return   AuroraUniverse.getInstance().getConfig().getBoolean("debug-mode");

    }

    public static boolean hasString(String path) {
        return AuroraUniverse.getLanguage().get(path) != null;
    }

}
