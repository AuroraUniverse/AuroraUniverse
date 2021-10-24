package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.RegionException;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Region;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.ResidentRegion;
import ru.etysoft.aurorauniverse.world.Town;

public class TownRegionCommand {


    public TownRegionCommand(CommandSender sender, Resident resident, String[] args)
    {
        if(args.length > 1)
        {
            String arg2 = args[1];

            if(arg2.equals("give") && args.length > 2)
            {
                giveRegion(sender, resident, args);
            }
            else if(arg2.equals("reset"))
            {
                resetRegion(sender, resident, args);
            }
            else if(arg2.equals("add") && args.length > 2)
            {
                addMember(sender, resident, args);
            }
            else if(arg2.equals("kick") && args.length > 2)
            {
                removeMember(sender, resident, args);
            }
        }
        else
        {
           regionInfo(sender, resident, args);
        }
    }

    public void regionInfo(CommandSender sender, Resident resident, String[] args)
    {
        if(!resident.hasTown()) return;
        if (Permissions.canEditTown(sender) | Permissions.canGetRegionInfo(sender)) {

            Town town = Towns.getTown( ((Player) sender).getLocation().getChunk());
            if (town != null) {
                Region region = town.getRegion(((Player) sender).getLocation());

                if(resident.getTown() == town) {
                    if (region != null) {
                        if (region instanceof ResidentRegion) {
                            ResidentRegion residentRegion = (ResidentRegion) region;

                            String membersString = "";

                            for(String nickname : residentRegion.getMembers())
                            {
                                membersString += nickname + "; ";
                            }

                           sender.sendMessage(AuroraConfiguration.getColorString("region-info.title"));
                            sender.sendMessage(AuroraConfiguration.getColorString("region-info.members")
                                    .replace("%s2",membersString)
                                    .replace("%s1", String.valueOf(residentRegion.getMembers().size()))
                                    .replace("%s", residentRegion.getOwner().getName()));
                        } else {
                            sender.sendMessage(AuroraConfiguration.getColorString("region-info.title"));
                            sender.sendMessage(AuroraConfiguration.getColorString("region-info.town-owned"));
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-unowned"), sender);
                    }
                }
                else
                {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-unowned"), sender);
            }
        }
        else
        {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
        }
    }

    public void giveRegion(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canGiveRegion(sender)) {
            Town town = resident.getTown();
            if (town != null) {
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion) {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-already-owned"), sender);
                    } else {
                        Resident receiver = Residents.getResident(args[2]);
                        if (receiver != null) {
                            try {
                                town.createPlayerRegion(((Player) sender).getLocation().getChunk(), receiver);
                                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-success"), sender);
                            } catch (RegionException e) {
                                e.printStackTrace();
                                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-error"), sender);
                            }
                        }
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-unowned"), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
        }
    }

    public static void addMember(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canAddMemberRegion(sender)) {
            Town town = resident.getTown();
            if (town != null) {
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion && args.length > 1) {
                        ResidentRegion residentRegion = (ResidentRegion) region;
                        Resident newMember = Residents.getResident(args[1]);
                        if (newMember != null && newMember != resident) {

                                if(residentRegion.getOwner() == resident | Permissions.canBypassRegion(sender))
                                {
                                    if(residentRegion.addMember(resident))
                                    {
                                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-added").replace("%s", args[2]), sender);
                                    }
                                    else
                                    {
                                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-owner-members"), sender);
                                    }
                                }
                                else
                                {
                                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-owner-members"), sender);
                                }



                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-error"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-townowned"), sender);

                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-unowned"), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
        }
    }

    public static void removeMember(CommandSender sender, Resident resident, String[] args) {
        if (Permissions.canKickMemberRegion(sender)) {
            Town town = resident.getTown();
            if (town != null) {
                Region region = town.getRegion(((Player) sender).getLocation());

                if (region != null) {
                    if (region instanceof ResidentRegion && args.length > 1) {
                        ResidentRegion residentRegion = (ResidentRegion) region;
                        Resident newMember = Residents.getResident(args[1]);
                        if (newMember != null && newMember != resident) {

                            if(residentRegion.getOwner() == resident | Permissions.canBypassRegion(sender))
                            {
                                if(residentRegion.removeMember(resident))
                                {
                                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-kicked").replace("%s", args[1]), sender);
                                }
                                else
                                {
                                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-owner-members"), sender);
                                }
                            }
                            else
                            {
                                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-owner-members"), sender);
                            }



                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-error"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-townowned"), sender);

                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-unowned"), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
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
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-reset-success"), sender);
                        } catch (RegionException e) {
                            e.printStackTrace();
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-error"), sender);
                        }


                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("region-unowned"), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
        }
    }

}
