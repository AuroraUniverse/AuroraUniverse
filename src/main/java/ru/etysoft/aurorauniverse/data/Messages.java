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

    public static String balance(String bal) {
        return AuroraConfiguration.getColorString("economy.balance").replace("%s", bal);
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

    public static String claimTooFar() {
        return AuroraConfiguration.getColorString("too-far");
    }

    public static String enablePerm(String groupname, String permission) {
        return AuroraConfiguration.getColorString("town-enperm").replace("%s", permission).replace("%k", groupname);
    }

    public static String disablePerm(String groupname, String permission) {
        return AuroraConfiguration.getColorString("town-disperm").replace("%s", permission).replace("%k", groupname);
    }

    public static String cantSetPerm(String groupname, String permission) {
        return AuroraConfiguration.getColorString("town-cantsetperm").replace("%s", permission).replace("%k", groupname);
    }

    public static String claimTooClose() {
        return AuroraConfiguration.getColorString("too-close");
    }
}
