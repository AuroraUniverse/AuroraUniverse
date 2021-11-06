package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.OutpostRegion;
import ru.etysoft.aurorauniverse.world.Region;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownClaimCommand {

    public TownClaimCommand(Resident resident, CommandSender sender, Player pl) {
        if (resident == null) {
            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
        } else {
            try {
                Town t = resident.getTown();
                if (Permissions.canClaim(pl)) {
                    try {
                        if(t.getBank().hasAmount(t.getNewChunkPrice()))
                        {
                            double priceNewChunk = t.getNewChunkPrice();
                            double priceOutpost = t.getNewOutpostPrice();
                            Chunk ch = pl.getLocation().getChunk();
                            Region region = t.claimChunk(ch, pl);
                            if (region != null) {
                                resident.setLastwild(false);
                                resident.setLastTown(t.getName());
                                if(region instanceof OutpostRegion)
                                {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-claim-outpost"), sender);

                                    if(t.withdrawBank(priceOutpost))
                                    {
                                        pl.playSound(pl.getLocation(), Sound.BLOCK_CHAIN_PLACE, 100.0f, 1.0f);
                                    }
                                    else
                                    {
                                        t.unclaimChunk(ch);
                                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-cant-withdraw-outpost"), sender);
                                        pl.playSound(pl.getLocation(), Sound.ENTITY_GHAST_SCREAM, 100.0f, 1.0f);
                                    }
                                    return;
                                }

                                t.withdrawBank(priceNewChunk);


                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-claim"), sender);
                                pl.playSound(pl.getLocation(), Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 100.0f, 1.0f);
                            } else {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-cantclaim"), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-cantclaim"), sender);
                        }

                    } catch (TownException e) {
                        e.printStackTrace();
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                }

            }
            catch (TownNotFoundedException e)
            {

                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
            }
        }
    }

}
