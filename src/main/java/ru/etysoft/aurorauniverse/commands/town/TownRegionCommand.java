package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.RegionException;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.*;

public class TownRegionCommand {


    public TownRegionCommand(CommandSender sender, Resident resident, String[] args) {
        if (args.length > 1) {
            String arg2 = args[1];

            if (arg2.equals("give") && args.length > 2) {
                giveRegion(sender, resident, args);
            } else if (arg2.equals("reset")) {
                resetRegion(sender, resident, args);
            } else if (arg2.equals("add") && args.length > 2) {
                addMember(sender, resident, args);
            } else if (arg2.equals("kick") && args.length > 2) {
                removeMember(sender, resident, args);
            } else if (arg2.equals("pvp") && args.length > 2) {
                togglePvp(sender, resident, args);
            } else if (arg2.equals("fire") && args.length > 2) {
                toggleFire(sender, resident, args);
            } else if (arg2.equals("explosions") && args.length > 2) {
                toggleExplosions(sender, resident, args);
            } else if (arg2.equals("mobs") && args.length > 2) {
                toggleMobs(sender, resident, args);
            }
        } else {
            regionInfo(sender, resident, args);
        }
    }

    public void regionInfo(CommandSender sender, Resident resident, String[] args) {
        if (!resident.hasTown()) return;
        if (Permissions.canEditTowns(sender) | Permissions.canGetRegionInfo(sender)) {

            Town town = Towns.getTown(ChunkPair.fromChunk(((Player) sender).getLocation().getChunk()));
            if (town != null) {
                Region region = town.getRegion(((Player) sender).getLocation());

                try {
                    if (resident.getTown() == town | sender.hasPermission("aun.admin")) {
                        if (region != null) {
                            if (region instanceof ResidentRegion) {
                                ResidentRegion residentRegion = (ResidentRegion) region;

                                String membersString = "";

                                for (String nickname : residentRegion.getMembers()) {
                                    membersString += nickname + "; ";
                                }

                                sender.sendMessage(AuroraLanguage.getColorString("region-info.title"));

                                sender.sendMessage(AuroraLanguage.getColorString("region-info.toggle-pvp").
                                        replace("%s", String.valueOf(residentRegion.isPvp())));
                                sender.sendMessage(AuroraLanguage.getColorString("region-info.toggle-fire").
                                        replace("%s", String.valueOf(residentRegion.isFire())));
                                sender.sendMessage(AuroraLanguage.getColorString("region-info.toggle-explosions").
                                        replace("%s", String.valueOf(residentRegion.isExplosions())));
                                sender.sendMessage(AuroraLanguage.getColorString("region-info.toggle-mobs").
                                        replace("%s", String.valueOf(residentRegion.isMobs())));

                                sender.sendMessage(AuroraLanguage.getColorString("region-info.members")
                                        .replace("%s2", membersString)
                                        .replace("%s1", String.valueOf(residentRegion.getMembers().size()))
                                        .replace("%s", residentRegion.getOwner().getName()));
                            } else {
                                sender.sendMessage(AuroraLanguage.getColorString("region-info.title"));
                                sender.sendMessage(AuroraLanguage.getColorString("region-info.town-owned"));
                            }
                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                    }
                } catch (TownNotFoundedException e) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

    public void giveRegion(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canGiveRegion(sender)) {

            try {
                Town town = resident.getTown();
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-already-owned"), sender);
                    } else if (region instanceof OutpostRegion) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-is-outpost"), sender);
                    } else {
                            Resident receiver = Residents.getResident(args[2]);
                            if (receiver != null) {
                                try {
                                    town.createPlayerRegion(ChunkPair.fromChunk(((Player) sender).getLocation().getChunk()), receiver);
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-success"), sender);
                                } catch (RegionException e) {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-error"), sender);
                                }
                            }
                        }
                    }
                else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                }
            } catch (TownNotFoundedException ignored) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

    public static void togglePvp(CommandSender sender, Resident resident, String[] args) {
        if (sender.hasPermission(Permissions.TOWN_REGION_TOGGLE)) {

//                Town town = resident.getTown();
//                Region region = town.getRegion(((Player) sender).getLocation());
                if (!(sender instanceof Player)) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("console"), sender);
                    return;
                }
                Player player = (Player) sender;
                Region region = Towns.getTown(player.getLocation().getChunk()).getRegion(player.getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion && args.length > 1) {
                        if (((ResidentRegion) region).getMembers().contains(resident.getName()) | region.getTown().getMayor().getName().equals(sender.getName())
                        | sender.hasPermission("aun.admin"))
                        {
                            ResidentRegion residentRegion = (ResidentRegion) region;
                            String status = args[2];

                            if (status.equalsIgnoreCase("on")) {
                                residentRegion.setPvp(true);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.Region.PVP_ON), sender);
                            } else if (status.equalsIgnoreCase("off")) {
                                residentRegion.setPvp(false);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.Region.PVP_OFF), sender);
                            }
                            else
                            {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-event-canceled"), sender);
                        }


                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-townowned"), sender);

                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                }

        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

    public static void toggleFire(CommandSender sender, Resident resident, String[] args) {
        if (sender.hasPermission(Permissions.TOWN_REGION_TOGGLE)) {

                if (!(sender instanceof Player)) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("console"), sender);
                    return;
                }
                Player player = (Player) sender;
                Region region = Towns.getTown(player.getLocation().getChunk()).getRegion(player.getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion && args.length > 1) {
                        if (((ResidentRegion) region).getMembers().contains(resident.getName()) | region.getTown().getMayor().getName().equals(sender.getName())
                                | sender.hasPermission("aun.admin"))
                        {
                            ResidentRegion residentRegion = (ResidentRegion) region;
                            String status = args[2];

                            if (status.equalsIgnoreCase("on")) {
                                residentRegion.setFire(true);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.Region.FIRE_ON), sender);
                            } else if (status.equalsIgnoreCase("off")) {
                                residentRegion.setFire(false);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.Region.FIRE_OFF), sender);
                            }
                            else
                            {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-event-canceled"), sender);
                        }


                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-townowned"), sender);

                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                }

        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

    public static void toggleExplosions(CommandSender sender, Resident resident, String[] args) {
        if (sender.hasPermission(Permissions.TOWN_REGION_TOGGLE)) {

                if (!(sender instanceof Player)) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("console"), sender);
                    return;
                }
                Player player = (Player) sender;
                Region region = Towns.getTown(player.getLocation().getChunk()).getRegion(player.getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion && args.length > 1) {
                        if (((ResidentRegion) region).getMembers().contains(resident.getName()) | region.getTown().getMayor().getName().equals(sender.getName())
                                | sender.hasPermission("aun.admin"))
                        {
                            ResidentRegion residentRegion = (ResidentRegion) region;
                            String status = args[2];

                            if (status.equalsIgnoreCase("on")) {
                                residentRegion.setExplosions(true);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.Region.EXPLOSIONS_ON), sender);
                            } else if (status.equalsIgnoreCase("off")) {
                                residentRegion.setExplosions(false);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.Region.EXPLOSIONS_OFF), sender);
                            }
                            else
                            {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-event-canceled"), sender);
                        }


                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-townowned"), sender);

                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                }

        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

    public static void toggleMobs(CommandSender sender, Resident resident, String[] args) {
        if (sender.hasPermission(Permissions.TOWN_REGION_TOGGLE)) {

                if (!(sender instanceof Player)) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("console"), sender);
                    return;
                }
                Player player = (Player) sender;
                Region region = Towns.getTown(player.getLocation().getChunk()).getRegion(player.getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion && args.length > 1) {
                        if (((ResidentRegion) region).getMembers().contains(resident.getName()) | region.getTown().getMayor().getName().equals(sender.getName())
                                | sender.hasPermission("aun.admin"))
                        {
                            ResidentRegion residentRegion = (ResidentRegion) region;
                            String status = args[2];

                            if (status.equalsIgnoreCase("on")) {
                                residentRegion.setMobs(true);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.Region.MOBS_ON), sender);
                            } else if (status.equalsIgnoreCase("off")) {
                                residentRegion.setMobs(false);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.Region.MOBS_OFF), sender);
                            }
                            else
                            {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-event-canceled"), sender);
                        }


                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-townowned"), sender);

                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                }

        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

    public static void addMember(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canAddMemberRegion(sender)) {

            try {
                Town town = resident.getTown();
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion && args.length > 2) {
                        ResidentRegion residentRegion = (ResidentRegion) region;
                        Resident newMember = Residents.getResident(args[2]);

                        if(!AuroraUniverse.getInstance().getConfig().getBoolean("region-allow-noresidents"))
                        {
                            if(!town.getResidents().contains(newMember))
                            {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-add-noresident-error"), sender);
                                return;
                            }
                        }

                        if (newMember != null && newMember != resident) {

                            if (residentRegion.getOwner() == resident | Permissions.canBypassRegion(sender)) {
                                if(residentRegion.getMembers().contains(newMember.getName()))
                                {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-add-contains"), sender);
                                }
                                else {
                                    if (residentRegion.addMember(newMember)) {
                                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-added").replace("%s", args[2]), sender);
                                    } else {
                                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-owner-members"), sender);


                                    }
                                }
                            } else {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-owner-members"), sender);
                            }


                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-error"), sender);
                        }
                    } else {
                        if(!(region instanceof ResidentRegion))
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-townowned"), sender);
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-error"), sender);
                        }


                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                }
            } catch (TownNotFoundedException ignored) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

    public static void removeMember(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canKickMemberRegion(sender)) {

            try {
                Town town = resident.getTown();
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion && args.length > 1) {
                        ResidentRegion residentRegion = (ResidentRegion) region;
                        Resident newMember = Residents.getResident(args[2]);
                        if (newMember != null && newMember != resident) {

                            if (residentRegion.getOwner() == resident | Permissions.canBypassRegion(sender)) {
                                if (residentRegion.removeMember(newMember)) {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-kicked").replace("%s", args[2]), sender);
                                } else {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-owner-members"), sender);
                                }
                            } else {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-owner-members"), sender);
                            }


                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-error"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-townowned"), sender);

                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                }
            } catch (TownNotFoundedException ignored) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

    public static void resetRegion(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canResetRegion(sender)) {

            try {
                Town town = resident.getTown();
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {

                    try {
                        town.resetRegion(ChunkPair.fromChunk(((Player) sender).getLocation().getChunk()));
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-reset-success"), sender);
                    } catch (RegionException e) {
                        e.printStackTrace();
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-error"), sender);
                    }


                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("region-unowned"), sender);
                }
            } catch (TownNotFoundedException ignored) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }

}
