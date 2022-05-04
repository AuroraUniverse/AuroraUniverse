package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;

public class EconomyCommands implements CommandExecutor {

    private Resident resident;
    private CommandSender sender;
    private Player player;
    private String[] args;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.args = args;


        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("pay")) {
                pay();
            } else if (args[0].equalsIgnoreCase("give")) {
                give();
            } else if (args[0].equalsIgnoreCase("take")) {
                take();
            } else {
                if (Permissions.canBalance(sender) && sender instanceof Player) {
                    Resident me = Residents.getResident((Player) sender);
                    Messaging.sendPrefixedMessage(Messages.balance(String.valueOf(me.getBalance())), sender);
                } else {
                    Messaging.sendPrefixedMessage(Messages.balance("good balance, dude (no limits, bro)"), sender);
                }
            }
        } else {
            //No arguments message
            if (Permissions.canBalance(sender) && sender instanceof Player) {
                Resident me = Residents.getResident((Player) sender);
                Messaging.sendPrefixedMessage(Messages.balance(String.valueOf(me.getBalance())), sender);
            } else {
                Messaging.sendPrefixedMessage(Messages.balance("good balance, dude (no limits, bro)"), sender);
            }
        }
        return true;
    }

    private void pay() {
        if (Permissions.canPay(sender)) {
            if (args.length > 2) {
                if (sender instanceof Player) {
                    Resident from = Residents.getResident((Player) sender);
                    Resident to = Residents.getResident(args[1]);
                    if (to != null) {
                        try {
                            double toPay = Double.valueOf(args[2]);
                            if(toPay > AuroraUniverse.getInstance().getConfig().getDouble("min-pay-amount")) {
                                if (from.takeBalance(toPay)) {
                                    to.giveBalance(toPay);
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.success").replace("%s", args[2]).replace("%k", args[1]), sender);
                                } else {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.no-money").replace("%s", toPay + ""), sender);
                                }
                            }
                            else
                            {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.error").replace("%s", args[2]).replace("%k", args[1]), sender);
                            }
                        } catch (Exception e) {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.error").replace("%s", args[2]).replace("%k", args[1]), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.not-found").replace("%s", args[1]), sender);
                    }
                } else {
                    Messaging.sendPrefixedMessage("Please, use &f/money give &band &f/money take&b from console!", sender);
                }
            } else {
                Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.noPermission(), sender);
        }
    }

    private void give() {
        if (Permissions.canGive(sender)) {
            if (args.length > 2) {
                Resident to = Residents.getResident(args[1]);
                if (to != null) {
                    try {
                        double toPay = Double.valueOf(args[2]);
                        to.giveBalance(toPay);
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.success").replace("%s", args[2]).replace("%k", args[1]), sender);
                    } catch (Exception e) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.error").replace("%s", args[2]).replace("%k", args[1]), sender);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.not-found").replace("%s", args[1]), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.noPermission(), sender);
        }
    }

    private void take() {
        if (Permissions.canTake(sender)) {
            if (args.length > 2) {
                Resident to = Residents.getResident(args[1]);
                if (to != null) {
                    try {
                        double toPay = Double.valueOf(args[2]);
                        to.takeBalance(toPay);
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.success").replace("%s", args[2]).replace("%k", args[1]), sender);
                    } catch (Exception e) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.error").replace("%s", args[2]).replace("%k", args[1]), sender);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.not-found").replace("%s", args[1]), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.noPermission(), sender);
        }
    }
}
