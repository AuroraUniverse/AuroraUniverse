package ru.etysoft.aurorauniverse.chat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.chat.AuroraChat;

public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (AuroraUniverse.getInstance().getConfig().getBoolean("use-aurora-chat")) {
            Logger.debug("Processing message " + event.getMessage());
            String message = event.getMessage();

            try {
                if (event.isCancelled()) return;

                Player playerSender = event.getPlayer();

                String processedMessage = AuroraChat.processMessage(message, playerSender, event.getRecipients(), false);


                event.getRecipients().clear();
                if (processedMessage == null) {
                    event.setCancelled(true);
                    return;
                }

                event.setFormat(processedMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }


}
