package ru.etysoft.aurorauniverse.data;

import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;

public class Messages {

    public static String noPermission()
    {
        return AuroraConfiguration.getColorString("access-denied-message");
    }

    public static String wrongArgs()
    {
        return AuroraConfiguration.getColorString("no-arguments");
    }

    public static String reload()
    {
        return AuroraConfiguration.getColorString("reloaded-message");
    }
}
