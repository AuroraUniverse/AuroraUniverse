package ru.etysoft.aurorauniverse;

import org.bukkit.configuration.file.FileConfiguration;

public class LanguageSetup {

    public static FileConfiguration setup(FileConfiguration l)
    {
        l.addDefault("authors-string", "Authors: ");
        l.addDefault("version-string", "Version: ");
        l.addDefault("apiversion-string", "API Version: ");
        l.addDefault("prefix", "&7[&bAuroraUniverse&7]");
        l.addDefault("reloaded-message", "&aPlugin was successfully reloaded with new params!");
        l.addDefault("access-denied-message", "&eYou have not permisson to do that!");
        l.addDefault("town-created-message", "You successfuly created town with name %s");
        l.addDefault("town-cantcreate-message", "&cCan't create town: %s ");
        l.addDefault("town-created-message", "You successfuly created town with name %s");
        l.addDefault("town-deleted-message", "Your town was successfully deleted");
        l.addDefault("town-teleported-to-spawn", "You have been teleported to the town spawn");
        l.addDefault("town-dont-belong", "\"You don't belong to town!");
        l.addDefault("town-claim", "Successfully claimed new chunk!");
        l.addDefault("town-cantclaim", "Can't claim a new chunk!");
        l.addDefault("town-unclaim", "Successfully unclaimed a townchunk!");
        l.addDefault("town-cantunclaim", "Can't unclaim a townchunk!");
        l.addDefault("town-leave", "You successfully leave town with name %s");
        l.addDefault("town-setspawn", "New spawnpoint set!");
        l.addDefault("town-cantsetspawn", "Can't set town spawnpoint!");
        l.addDefault("town-kick", "Resident %s has kicked from the town");
        l.addDefault("town-invite", "Resident %s has added to the town");
        l.addDefault("town-pvpon", "Now town has PvP enabled");
        l.addDefault("town-pvpoff", "Now town has PvP disabled");
        l.addDefault("town-pvp", "You can't PvP here!");
        l.addDefault("town-block-place", "You can't place blocks here!");
        l.addDefault("town-block-break", "You can't break blocks here!");
        l.addDefault("town-mayor", "Mayor: ");
        l.addDefault("town-residents", "Residents(");
        l.addDefault("town-chunks", "Chunks: ");
        l.addDefault("e1", "You can't PvP here!");
        l.addDefault("e2", "wrong name");
        l.addDefault("e3", "town with name %s already exists");
        l.addDefault("e4", "already in town");
        l.addDefault("e5", "chunk don't belong to the town");
        l.addDefault("town-welcome", "Welcome to the town %s");
        l.addDefault("world", "You have entered the world! Be careful!");

        return  l;
    }
}
