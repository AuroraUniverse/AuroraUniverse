package ru.etysoft.aurorauniverse.data;

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

    public static String adminTownDelete(String townname) {
        return AuroraConfiguration.getColorString("admin.town-delete").replace("%s", townname);
    }

    public static String adminCantTownDelete(String townname) {
        return AuroraConfiguration.getColorString("admin.town-cantdelete").replace("%s", townname);
    }

    public static String reload() {
        return AuroraConfiguration.getColorString("reloaded-message");
    }

    public static String cantConsole() {
        return AuroraConfiguration.getColorString("console");
    }
}
