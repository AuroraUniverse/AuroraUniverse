package ru.etysoft.aurorauniverse.placeholders;

import com.mysql.jdbc.log.Log;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;

public class PlaceholderFormatter {
    public static String process(String string, Player player)
    {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            string = PlaceholderAPI.setPlaceholders(player, string);

            return string;
        }
        Logger.warning("Placeholder API seems to not be initialized");
        return string;
    }
}
