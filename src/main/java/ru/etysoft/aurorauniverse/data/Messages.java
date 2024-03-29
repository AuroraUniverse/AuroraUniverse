package ru.etysoft.aurorauniverse.data;

import ru.etysoft.aurorauniverse.utils.AuroraLanguage;

public class Messages {

    public static String noPermission()
    {
        return AuroraLanguage.getColorString("access-denied-message");
    }

    public static String wrongArgs()
    {
        return AuroraLanguage.getColorString("no-arguments");
    }

    public static String balance(String bal) {
        return AuroraLanguage.getColorString("economy.balance").replace("%s", bal);
    }

    public static String adminTownDelete(String townname) {
        return AuroraLanguage.getColorString("admin.town-delete").replace("%s", townname);
    }

    public static String adminCantTownDelete(String townname) {
        return AuroraLanguage.getColorString("admin.town-cantdelete").replace("%s", townname);
    }

    public static String reload() {
        return AuroraLanguage.getColorString("reloaded-message");
    }

    public static String cantConsole() {
        return AuroraLanguage.getColorString("console");
    }

    public static String claimTooFar() {
        return AuroraLanguage.getColorString("too-far");
    }

    public static String enablePerm(String groupname, String permission) {
        return AuroraLanguage.getColorString("town-enperm").replace("%s", permission).replace("%k", groupname);
    }

    public static String disablePerm(String groupname, String permission) {
        return AuroraLanguage.getColorString("town-disperm").replace("%s", permission).replace("%k", groupname);
    }

    public static String cantSetPerm(String groupname, String permission) {
        return AuroraLanguage.getColorString("town-cantsetperm").replace("%s", permission).replace("%k", groupname);
    }

    public static String claimTooClose() {
        return AuroraLanguage.getColorString("too-close");
    }


    public static class Keys
    {
        public final static String WRONG_ARGS = "no-arguments";
        public final static String ACCESS_DENIED = "access-denied-message";
        public final static String NOT_REGISTERED_TOWN = "not-registered-town";
        public final static String NOT_REGISTERED_RESIDENT = "not-registered-resident";
        public final static String TOWN_WELCOME = "town-welcome";
        public final static String TOWN_DONT_BELONG = "town-dont-belong";
        public final static String REGION_WELCOME = "region-welcome";
        public final static String TOWN_REGION_WELCOME = "town-region-welcome";
        public final static String REP_PLUS = "rep-plus";
        public final static String REP_MINUS = "rep-minus";
        public final static String REP_CANT_CHANGE = "rep-cantchange";

        public static class Region
        {
            public static String PVP_OFF = "region-pvp-off";
            public static String PVP_ON = "region-pvp-on";

            public static String FIRE_OFF = "region-fire-off";

            public static String FIRE_ON = "region-fire-on";

            public static String EXPLOSIONS_OFF = "region-explosions-off";

            public static String EXPLOSIONS_ON = "region-explosions-on";

            public static String MOBS_OFF = "region-mobs-off";

            public static String MOBS_ON = "region-mobs-on";
        }

        public static class Admin
        {
            public static String TOWN_GAVE_BONUS = "admin.town-gave-bonus";
            public static String TOWN_ADDED = "admin.town-added";
            public static String TOWN_REMOVED = "admin.town-removed";
            public static String TOWN_CANT_ADD = "admin.town-cant-add";
            public static String TOWN_CANT_REMOVE = "admin.town-cant-remove";
            public static String TOWN_CANT_GIVE_BONUS = "admin.town-cant-give-bonus";
            public static String TOWN_DEPOSITED = "admin.town-deposited";
            public static String TOWN_CANT_DEPOSIT = "admin.town-cant-deposit";
            public static String TOWN_WITHDRAW = "admin.town-withdraw";
            public static String TOWN_CANT_WITHDRAW = "admin.town-cant-withdraw";

            public static String TOWN_DELETED = "admin.town-deleted";
            public static String TOWN_CANT_DELETE = "admin.town-cantdelete";
        }
    }
}
