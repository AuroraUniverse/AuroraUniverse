package ru.etysoft.aurorauniverse.commands.town;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.awt.*;

public class TownInviteCommand {

    public TownInviteCommand(String[] args, Resident resident, CommandSender sender) {
        if (args.length > 1) {
            if (resident != null) {
                Player de = (Player) Bukkit.getPlayer(args[1]);
                Resident resident2 = Residents.getResident(de);
                try {
                    if (resident2 == null) throw new TownNotFoundedException();
                    if(resident2.hasTown())  {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("e4").replace("%s", resident2.getName()), sender);
                        return;
                    }
                    Town t = resident.getTown();
                    if (Permissions.canInviteResident(sender)) {
                        if (!t.getInvitedResidents().contains(resident2)) {
                            t.getInvitedResidents().add(resident2);
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-invite").replace("%s", resident2.getName()), sender);
                            if (Bukkit.getPlayer(sender.getName()) != null) {
                                TextComponent msgJSON = new TextComponent(AuroraLanguage.getColorString("accept"));
                                msgJSON.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(AuroraLanguage.getColorString("town-accept-json")).create()));
                                msgJSON.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/t accept " + t.getName()));

                                TextComponent msg = new TextComponent(AuroraUniverse.getPrefix() + " " + AuroraLanguage.getColorString("town-request").replace("%s", t.getName()));
                                ComponentBuilder builder = new ComponentBuilder();

                                builder.append(msg);
                                builder.append("\n");
                                builder.append(msgJSON);

                                resident2.getPlayer().spigot().sendMessage(builder.create());
                            }
                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-accept-err"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                    }
                } catch (TownNotFoundedException ignored) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                }
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
        }
    }

}
