package ru.etysoft.aurorauniverse.utils;

import org.bukkit.ChatColor;
import ru.etysoft.aurorauniverse.Logger;

public class ColorCodes {
    //Convert string with color codes to chatcolor string
    public static String toColor(String text)
    {
        try
        {
            return  ChatColor.translateAlternateColorCodes('&', text);
        }
      catch (Exception e)
      {
          Logger.info("Can't convert string '" + text + "' to color. Use simple string without colors.");
          return text;
      }
    }
}
