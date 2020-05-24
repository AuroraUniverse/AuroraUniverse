package ru.etysoft.aurorauniverse;

import org.bukkit.ChatColor;

public class fun {
    //Convert string with color codes to chatcolor string
    public static String cstring(String text)
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
