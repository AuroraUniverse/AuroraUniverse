package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.RegionException;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Region;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.ResidentRegion;
import ru.etysoft.aurorauniverse.world.Town;

public class TownRegionCommand {

    public static void giveRegion(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canGiveRegion(sender)) {
            Town town = resident.getTown();
            if (town != null) {
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion) {
                        Messaging.mess(AuroraConfiguration.getColorString("region-already-owned"), sender);
                    } else {
                        Resident receiver = Residents.getResident(args[1]);
                        if (receiver != null) {
                            try {
                                town.createPlayerRegion(((Player) sender).getLocation().getChunk(), receiver);
                                Messaging.mess(AuroraConfiguration.getColorString("region-success"), sender);
                            } catch (RegionException e) {
                                e.printStackTrace();
                                Messaging.mess(AuroraConfiguration.getColorString("region-error"), sender);
                            }
                        }
                    }
                } else {
                    Messaging.mess(AuroraConfiguration.getColorString("region-unowned"), sender);
                }
            } else {
                Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), sender);
        }
    }

    public static void resetRegion(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canResetRegion(sender)) {
            Town town = resident.getTown();
            if (town != null) {
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {

                        try {
                            town.resetRegion(((Player) sender).getLocation().getChunk());
                            Messaging.mess(AuroraConfiguration.getColorString("region-reset-success"), sender);
                        } catch (RegionException e) {
                            e.printStackTrace();
                            Messaging.mess(AuroraConfiguration.getColorString("region-error"), sender);
                        }


                } else {
                    Messaging.mess(AuroraConfiguration.getColorString("region-unowned"), sender);
                }
            } else {
                Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), sender);
        }
    }

}
