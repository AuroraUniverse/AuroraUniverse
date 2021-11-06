package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class AdminCommands extends CommandPattern {

    public AdminCommands(CommandSender sender, Resident resident, String[] args) {
        super(sender, resident, args);

        // /aun town(0) action(1) name(2)

        if (Permissions.canEditTown(sender)) {
            if (args.length < 3) {
                sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS));
            } else {
                String action = args[1].toLowerCase();
                String name;
                try {
                    switch (action) {
                        case "delete":
                            name = Messaging.getStringFromArgs(args, 2);
                            delete(name);
                            break;
                        case "spawn":
                            name = Messaging.getStringFromArgs(args, 2);
                            spawn(name);
                            break;
                        case "outpost":
                            name = Messaging.getStringFromArgs(args, 3);
                            outpost(name);
                            break;
                        case "givebonus":
                            name = Messaging.getStringFromArgs(args, 3);
                            giveBonus(name);
                            break;
                        case "add":
                            name = Messaging.getStringFromArgs(args, 3);
                            add(name);
                            break;
                        case "remove":
                            name = Messaging.getStringFromArgs(args, 3);
                            remove(name);
                            break;
                        case "withdraw":
                            name = Messaging.getStringFromArgs(args, 3);
                            withdraw(name);
                            break;
                        case "deposit":
                            name = Messaging.getStringFromArgs(args, 3);
                            deposit(name);
                            break;
                    }
                } catch (TownNotFoundedException ignored) {
                    sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_TOWN));
                }
            }
        } else {
            sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.ACCESS_DENIED));
        }

    }

    private void outpost(String townName) throws TownNotFoundedException {
        Town town = Towns.getTown(townName);
        if(getSender() instanceof Player)
        {
            try
            {
                int num = Integer.parseInt(getArgs()[2]);
                town.teleportToOutpost((Player) getSender(), num);
            }
            catch (Exception e)
            {

            }

        }

    }

    private void spawn(String townName) throws TownNotFoundedException {
        Town town = Towns.getTown(townName);
        if(getSender() instanceof Player)
        {
            town.teleportToTownSpawn((Player) getSender());
        }

    }

    private void delete(String townName) throws TownNotFoundedException {
        Town town = Towns.getTown(townName);
        if (town.delete()) {
            getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_DELETED).replace("%s", townName));
        } else {
            getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_CANT_DELETE));
        }

    }

    private void giveBonus(String townName) throws TownNotFoundedException {
        Town town = Towns.getTown(townName);
        try {
            int bonusCount = Integer.parseInt(getArgs()[2]);
            if (bonusCount > 0) {
                town.setBonusChunks(town.getBonusChunks() + bonusCount);

                getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_GAVE_BONUS).replace("%s", String.valueOf(bonusCount))
                        .replace("%t", townName));
            }
            else
            {
                getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_CANT_GIVE_BONUS));
            }
        } catch (Exception ignored) {
            getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_CANT_GIVE_BONUS));
        }
    }

    private void add(String townName) throws TownNotFoundedException {
        Town town = Towns.getTown(townName);

        Resident resident = Residents.getResident(getArgs()[2]);
        if (resident != null) {
            if (!town.addResident(resident)) {
                getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_CANT_ADD).replace("%s", resident.getName()));
            } else {
                getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_ADDED).replace("%s", resident.getName()).
                        replace("%t", townName));
            }
        } else {
            getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_RESIDENT));
        }

    }

    private void remove(String townName) throws TownNotFoundedException {
        Town town = Towns.getTown(townName);

        Resident resident = Residents.getResident(getArgs()[2]);
        if (resident != null) {
            if (!town.removeResident(resident)) {
                getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_CANT_REMOVE).replace("%s", resident.getName()));
            } else {
                getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_REMOVED).replace("%s", resident.getName()).
                        replace("%t", townName));
            }
        } else {
            getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_RESIDENT));
        }

    }

    private void deposit(String townName) throws TownNotFoundedException {
        Town town = Towns.getTown(townName);
        try {
            int moneySum = Integer.parseInt(getArgs()[2]);
            if (moneySum > 0) {
                town.getBank().deposit( moneySum);
                getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_DEPOSITED).replace("%s", String.valueOf(moneySum))
                        .replace("%t", townName));
            }
            else
            {
                getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_CANT_DEPOSIT));
            }
        } catch (Exception ignored) {
            getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_CANT_DEPOSIT));
        }
    }


    private void withdraw(String townName) throws TownNotFoundedException {
        Town town = Towns.getTown(townName);
        try {
            int moneySum = Integer.parseInt(getArgs()[2]);
            if (moneySum > 0) {
                if(town.getBank().withdraw(moneySum))
                {
                    getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_WITHDRAW).replace("%s", String.valueOf(moneySum))
                            .replace("%t", townName));
                    return;
                }
            }
        } catch (Exception ignored) {
        }
        getSender().sendMessage(AuroraLanguage.getColorString(Messages.Keys.Admin.TOWN_CANT_WITHDRAW));
    }

}
