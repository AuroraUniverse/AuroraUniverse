package ru.etysoft.aurorauniverse.commands;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownCommands implements CommandExecutor {


     private static FileConfiguration language;

    public static void NewTownCommand(CommandSender sender, String[] args)
    {
         language = AuroraUniverse.getLanguage();
        if (args.length > 1) {
            try {
                Player pl = (Player) sender;
                StringBuilder name = new StringBuilder();
                for (String arg :
                        args) {
                    if (!arg.equals(args[0])) {
                        name.append(arg).append(" ");
                    }
                }
                try {
                   String townname = name.toString().replace("&", "");
                    if(Towns.createTown(townname, pl)) {
                        Messaging.mess(AuroraConfiguration.getColorString("town-created-message").replace("%s", name), sender);

                        Residents.getResident(pl).setLastwild(false);
                    }

                } catch (TownException e) {
                    Messaging.mess(AuroraConfiguration.getColorString("town-cantcreate-message").replace("%s", e.getMessageErr()), pl);
                }
            } catch (Exception e)
            {

                if(!(sender instanceof Player))
                {
                    Town newtown = new Town();


                    AuroraUniverse.getTownlist().put(args[1], newtown);
                }
                else
                {
                    Logger.error("Town creating error: ");
                    e.printStackTrace();
                    Messaging.mess("Error!", sender);
                }
            }
        } else {
            Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player pl = (Player) sender;
        Resident resident = Residents.getResident(pl);
        if(args.length > 0) {
           if(args[0].equalsIgnoreCase("new")) {
               TownCommands.NewTownCommand(sender, args);
           }
           else if(args[0].equalsIgnoreCase("delete")) {
               try {

                   assert resident != null;
                   if (resident.getTown().isMayor(resident)) {
                       if (resident.getTown().delete()) {
                           Messaging.mess(ColorCodes.toColor(AuroraConfiguration.getColorString("town-deleted-message")), pl);
                       }

                   } else {
                       Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), sender);
                   }
               } catch (Exception e) {
                   Messaging.mess(AuroraConfiguration.getColorString("town-cantcreate-message").replace("%s", e.getMessage()), pl);
               }
           }
           else if(args[0].equalsIgnoreCase("spawn")) {
               if (resident.hasTown()) {
                   Town t = resident.getTown();
                   t.teleportToTownSpawn(pl);
                   Messaging.mess(AuroraConfiguration.getColorString("town-teleported-to-spawn"), sender);
               } else {
                   //TODO: resident don't belong to
                   Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
               }
           }
           else if(args[0].equalsIgnoreCase("list")) {
               pl.sendMessage(ColorCodes.toColor(AuroraConfiguration.getColorString("town-list")));
               int page = 1;
               if (args.length > 1) {
                   try {
                       page = Integer.parseInt(args[1]);
                   } catch (Exception e) {
                       Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
                   }
               }
               final int[] i = {1};
               int finalPage = page;
               double d = Double.parseDouble("" + AuroraUniverse.getTownlist().size());
               double maxPage = Math.ceil((double) d / 10f);
               AuroraUniverse.getTownlist().forEach((name, town) -> {

                   if (i[0] != (10 * finalPage) + 1) {
                       if (i[0] > maxPage - 1) {
                           try {
                               pl.sendMessage(ChatColor.AQUA + name + ChatColor.GOLD + "(" + town.getMembersCount() + ", " + town.getMayor().getName() + ")");
                           } catch (Exception e) {
                               e.printStackTrace();
                               pl.sendMessage(name);
                           }


                       }
                   }
                   i[0]++;
               });

               Messaging.mess(AuroraConfiguration.getColorString("town-pages").replace("%s", String.valueOf(page)).replace("%y", AuroraUniverse.getTownlist().size() + ""), pl);
           }
           else if(args[0].equalsIgnoreCase("claim")) {
               if (resident.hasTown()) {
                   Town t = resident.getTown();
                   if (t.getMayor() == resident) {
                       try {
                           if (t.claimChunk(pl.getLocation().getChunk())) {
                               Messaging.mess(AuroraConfiguration.getColorString("town-claim"), sender);
                               resident.setLastwild(false);
                               resident.setLastTown(t.getName());
                           } else {
                               Messaging.mess(AuroraConfiguration.getColorString("town-cantclaim"), sender);
                           }
                       } catch (TownException e) {
                           e.printStackTrace();
                       }
                   }
                   // TODO: permissions claim
               }
           }
           else if(args[0].equalsIgnoreCase("unclaim")) {
               if (resident.hasTown()) {
                   Town t = resident.getTown();
                   if (t.getMayor() == resident) {

                       if (t.unclaimChunk(pl.getLocation().getChunk())) {
                           Messaging.mess(AuroraConfiguration.getColorString("town-unclaim"), sender);
                           resident.setLastwild(true);
                           Towns.ChangeChunk(pl, pl.getLocation().getChunk());
                       } else {
                           Messaging.mess(AuroraConfiguration.getColorString("town-cantunclaim"), sender);
                       }

                   }
                   // TODO: permissions claim
               }
           }
           else if(args[0].equalsIgnoreCase("leave")) {
               if (resident.hasTown()) {
                   Town t = resident.getTown();
                   t.removeResident(resident);
                   Messaging.mess(AuroraConfiguration.getColorString("town-leave").replace("%s", t.getName()), pl);

                   // TODO: permissions claim
               }
           }
           else if(args[0].equalsIgnoreCase("deposit")) {
               if (args.length > 1) {
                   if (resident.hasTown()) {
                       Town t = resident.getTown();
                       double d = 0;
                       try {
                           d = Double.valueOf(args[1]);

                       } catch (Exception e) {
                           Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
                           return true;
                       }
                       if (resident.takeBalance(d)) {
                           t.depositBank(d);
                           Messaging.mess(AuroraConfiguration.getColorString("town-deposit").replace("%s", d + ""), pl);
                       } else {
                           Messaging.mess(AuroraConfiguration.getColorString("town-cantdeposit").replace("%s", d + ""), pl);
                       }
                       // TODO: permissions claim
                   } else {
                       Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                   }
               } else {
                   Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
               }

           }
           else if(args[0].equalsIgnoreCase("withdraw")) {
               if (args.length > 1) {
                   if (resident.hasTown()) {
                       Town t = resident.getTown();
                       double d = 0;
                       try {
                           d = Double.valueOf(args[1]);

                       } catch (Exception e) {
                           Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
                           return true;
                       }

                       if (t.withdrawBank(d)) {
                           Messaging.mess(AuroraConfiguration.getColorString("town-withdraw").replace("%s", d + ""), pl);
                           resident.giveBalance(d);
                       } else {
                           Messaging.mess(AuroraConfiguration.getColorString("town-cantwithdraw").replace("%s", d + ""), pl);
                       }
                       // TODO: permissions claim
                   } else {
                       Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                   }
               } else {
                   Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
               }

           }
           else if(args[0].equalsIgnoreCase("set")) {
               if (args.length > 1) {
                   if (args[1].equalsIgnoreCase("spawn")) {
                       if (resident.hasTown()) {
                           Town t = resident.getTown();
                           if (t.getMayor() == resident) {
                               // TODO: permissions set
                               try {
                                   t.setSpawn(pl.getLocation());
                                   Messaging.mess(AuroraConfiguration.getColorString("town-setspawn"), pl);
                               } catch (TownException e) {
                                   Messaging.mess(AuroraConfiguration.getColorString("town-cantsetspawn").replace("%s", e.getMessageErr()), pl);

                               }

                           } else {
                               Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), pl);
                           }
                       } else {
                           //TODO: resident don't belong to town
                           Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                       }
                   }
               }
           }
           else  if(args[0].equalsIgnoreCase("kick")) {
               if (args.length > 1) {

                   Player de = (Player) Bukkit.getPlayer(args[1]);
                   Resident resident2 = Residents.getResident(de);
                   if (resident.hasTown() && resident2 != null) {
                       Town t = resident.getTown();
                       if (t.getMayor() == resident) {
                           // TODO: permissions set
                           t.removeResident(resident2);
                           Messaging.mess(AuroraConfiguration.getColorString("town-kick").replace("%s", resident2.getName()), pl);


                       } else {
                           Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), pl);
                       }
                   } else {
                       //TODO: resident don't belong to town
                       Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                   }
               }
           }
           else if(args[0].equalsIgnoreCase("invite")) {
               if (args.length > 1) {

                   Player de = (Player) Bukkit.getPlayer(args[1]);
                   Resident resident2 = Residents.getResident(de);
                   if (resident.hasTown() && resident2 != null) {
                       Town t = resident.getTown();
                       if (t.getMayor() == resident) {
                           // TODO: permissions set
                           t.addResident(resident2);
                           Messaging.mess(AuroraConfiguration.getColorString("town-invite").replace("%s", resident2.getName()), pl);

                       } else {
                           Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), pl);
                       }
                   } else {
                       //TODO: resident don't belong to town
                       Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                   }
               }
           }
           else if(args[0].equalsIgnoreCase("toggle")) {
               if (args.length > 2) {
                   if (args[1].equalsIgnoreCase("pvp")) {

                       if (resident.hasTown()) {
                           Town t = resident.getTown();
                           if (t.getMayor() == resident) {
                               // TODO: permissions set
                               if (args[2].equals("on")) {
                                   t.setPvP(true);
                                   Messaging.mess(AuroraConfiguration.getColorString("town-pvpon"), pl);
                               } else if (args[2].equals("off")) {
                                   t.setPvP(false);
                                   Messaging.mess(AuroraConfiguration.getColorString("town-pvpoff"), pl);
                               }


                           } else {
                               Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), pl);
                           }
                       } else {
                           //TODO: resident don't belong to town
                           Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                       }
                   }
               } else {
                   // TODO: no argument message
                   Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
               }
           }
           else
           {
                        Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
           }
            }
        else
            {

                if (resident.hasTown()) {
                    Messaging.towninfo(sender, resident.getTown());
                } else {
                    //TODO: no town message
                    Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                }

            }

        return true;

    }
}
