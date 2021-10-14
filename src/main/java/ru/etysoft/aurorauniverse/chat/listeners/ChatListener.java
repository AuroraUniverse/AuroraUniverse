package ru.etysoft.aurorauniverse.chat.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.chat.AuroraChat;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Logger.debug("Processing message " + event.getMessage());
        String message = event.getMessage();

        Resident resident = Residents.getResident(event.getPlayer().getName());

        Player playerSender = event.getPlayer();


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
                if (recipients.size() > 1) {
                    recipients.clear();
                    recipients.addAll(recipients);
                } else {
                    playerSender.sendMessage(AuroraConfiguration.getColorString("chat.local-nobody"));
                }

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
                        recipients.add(Bukkit.getPlayer(townResident.getName()));
                }
                    recipients.clear();
                    recipients.addAll(recipients);

            }

        }

    }
}
