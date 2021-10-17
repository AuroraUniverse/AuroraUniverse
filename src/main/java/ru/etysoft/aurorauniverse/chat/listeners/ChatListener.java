package ru.etysoft.aurorauniverse.chat.listeners;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.chat.AuroraChat;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.placeholders.PlaceholderFormatter;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;

import java.security.SecureRandom;
import java.util.*;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Logger.debug("Processing message " + event.getMessage());
        String message = event.getMessage();

        Resident resident = Residents.getResident(event.getPlayer().getName());
        Set<Player> finalRecipients = new HashSet<>(event.getRecipients());
        Player playerSender = event.getPlayer();
        event.getRecipients().clear();


        if (resident != null) {
            int channel = resident.getChatMode();
            if (channel == AuroraChat.Channels.GLOBAL) {
                event.setFormat(AuroraConfiguration.getColorString("chat.global").replace("%sender%", event.getPlayer().getName())
                        .replace("%message%", event.getMessage()));
            } else if (channel == AuroraChat.Channels.LOCAL) {
                event.setFormat(AuroraConfiguration.getColorString("chat.local").replace("%sender%", event.getPlayer().getName())
                        .replace("%message%", event.getMessage()));
                Set<Player> recipients = new HashSet<>();
                for (Player player : Bukkit.getOnlinePlayers()) {


                    if (playerSender.getLocation().distance(player.getLocation()) < 20) {
                        recipients.add(player);
                    }
                }
                if (recipients.size() == 1) {
                    playerSender.sendMessage(AuroraConfiguration.getColorString("chat.local-nobody"));
                }
                finalRecipients = recipients;

            } else if (channel == AuroraChat.Channels.TOWN) {

                event.setFormat(AuroraConfiguration.getColorString("chat.town").replace("%sender%", event.getPlayer().getName())
                        .replace("%message%", event.getMessage()));
                Set<Player> recipients = new HashSet<>();
                if(!resident.hasTown())
                {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), playerSender);
                    event.setCancelled(true);
                    return;
                }
                for (Resident townResident : resident.getTown().getResidents()) {
                    if(Bukkit.getOnlinePlayers().contains(resident.getName()))
                    {
                        recipients.add(Bukkit.getPlayer(townResident.getName()));
                    }

                }

                    finalRecipients.addAll(recipients);


            }

        }

        for(Player player : finalRecipients)
        {
            player.spigot().sendMessage(sendPlayerMessage(event.getFormat(), event.getPlayer()));
        }

    }

    private static TextComponent sendPlayerMessage(String message, Player player)
    {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                PlaceholderFormatter.process(AuroraConfiguration.getColorString("chat.hover-text"), player)).create() ) );
        return textComponent;
    }
}
