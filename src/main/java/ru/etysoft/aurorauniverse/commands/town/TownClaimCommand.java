package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownClaimCommand {

    public TownClaimCommand(Resident resident, CommandSender sender, Player pl) {
        if (resident == null) {
            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
        } else {
            if (resident.hasTown()) {
                Town t = resident.getTown();
                if (Permissions.canClaim(pl)) {
                    try {
                        if(t.getBank().withdraw(t.getNewChunkPrice()))
                        {
                            if (t.claimChunk(pl.getLocation().getChunk())) {
                                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-claim"), sender);
                                resident.setLastwild(false);
                                resident.setLastTown(t.getName());
                                pl.playSound(pl.getLocation(), Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 100.0f, 1.0f);
                            } else {
                                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-cantclaim"), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-cantclaim"), sender);
                        }

                    } catch (TownException e) {
                        e.printStackTrace();
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }

            }
        }
    }

}
