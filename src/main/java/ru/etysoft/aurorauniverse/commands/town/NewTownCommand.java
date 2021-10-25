package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class NewTownCommand {

    public NewTownCommand(CommandSender sender, String[] args) {
        FileConfiguration language = AuroraUniverse.getLanguage();
        if (args.length > 1) {
            try {
                Player pl = (Player) sender;
                if (Permissions.canCreateTown(pl)) {
                    StringBuilder name = new StringBuilder();
                    int i = 0;
                    for (String arg :
                            args) {
                        if (!arg.equals(args[0])) {
                            if (i != args.length - 1) {
                                name.append(arg).append(" ");
                            } else {
                                name.append(arg);
                            }
                        }
                        i++;
                    }
                    if(!Towns.isNameValid(name.toString()))
                    {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("name-invalid").replace("%s", name.toString()), sender);
                        return;
                    }
                    try {
                        double newTownPrice = AuroraUniverse.getInstance().getConfig().getDouble("price-new-town");
                        if (Residents.getResident(pl).getBalance() >= newTownPrice) {
                            String townname = name.toString().replace("&", "").replace("%", "");
                            if (Towns.createTown(townname, pl)) {
                                Resident resident = Residents.getResident(pl);
                                resident.setBalance(resident.getBalance() - newTownPrice);
                                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-created-message").replace("%s", name), sender);
                                pl.playSound(pl.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 100.0f, 1.0f);
                                Residents.getResident(pl).setLastwild(false);
                            }
                        } else {
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("price-error").replace("%s", String.valueOf(newTownPrice)), sender);
                        }

                    } catch (TownException e) {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-cantcreate-message").replace("%s", e.getErrorMessage()), pl);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            } catch (Exception e) {

                if (!(sender instanceof Player)) {
                    Town newtown = new Town();


                    AuroraUniverse.getTownList().put(args[1], newtown);
                } else {
                    Logger.error("Town creating error: ");
                    e.printStackTrace();
                    Messaging.sendPrefixedMessage("Error!", sender);
                }
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-arguments"), sender);
        }
    }
}
