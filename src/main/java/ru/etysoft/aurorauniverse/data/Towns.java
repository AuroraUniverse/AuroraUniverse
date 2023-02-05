package ru.etysoft.aurorauniverse.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.events.NewTownEvent;
import ru.etysoft.aurorauniverse.events.PlayerEnterTownEvent;
import ru.etysoft.aurorauniverse.events.PreTownCreateEvent;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.world.*;

import java.util.*;

public class Towns {

    public static Town getTown(String name) throws TownNotFoundedException {
        if (AuroraUniverse.townList != null) {
            if (name == null) throw new TownNotFoundedException();
            if (AuroraUniverse.townList.containsKey(name)) {
                return AuroraUniverse.townList.get(name);
            } else {
                throw new TownNotFoundedException();
            }
        } else {
            Logger.warning("Townlist is null!");
            throw new TownNotFoundedException();
        }
    }

    public static Town getTownById(String id) throws TownNotFoundedException {
        if (AuroraUniverse.townList != null) {
            if (id == null) throw new TownNotFoundedException();
            for(Town town : getTowns())
            {
                if(town.getId().equals(id))
                {
                    return town;
                }
            }
        } else {
            Logger.warning("Townlist is null!");
            throw new TownNotFoundedException();
        }

        return null;
    }

    public static boolean isTownExists(String s) {
        if (AuroraUniverse.townList.containsKey(s)) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<Town> getTownsFromBiggest() {
        ArrayList<Town> sorted = new ArrayList<Town>();
        Collection<Town> townsList = getTowns();
        Town[] array = new Town[townsList.size()];
        townsList.toArray(array);
        for (int i = array.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {

                if (array[j].getResidents().size() > array[j + 1].getResidents().size()) {
                    Town tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
        }

        Logger.debug("Towns : " + array.length);

        for(Town town : array)
        {
            sorted.add(town);
        }
        Collections.reverse(sorted);
        return sorted;
    }


    public static boolean hasMyTown(ChunkPair chunk, Town town) {
        if (AuroraUniverse.containsChunk(chunk)) {
            if (AuroraUniverse.getTownBlocks().get(chunk).getTown() == town) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean hasTown(ChunkPair chunk) {
        if (AuroraUniverse.containsChunk(chunk)) {
            return true;
        } else {
            return false;
        }
    }


    public static Town getTown(Chunk chunk) {
        return getTown(ChunkPair.fromChunk(chunk));
    }


    public static Town getTown(ChunkPair chunk) {
        if (AuroraUniverse.containsChunk(chunk)) {
            return AuroraUniverse.getTownBlocks().get(chunk).getTown();
        } else {
            return null;
        }
    }

    public static boolean hasTown(Location lc) {
        ChunkPair chunkPair = ChunkPair.fromChunk(lc.getChunk());
        if (AuroraUniverse.containsChunk(chunkPair)) {
            return true;
        } else {
            return false;
        }
    }

    public static Collection<Town> getTowns() {
        return AuroraUniverse.townList.values();
    }


    private static void handleRegionChange(Player player, Region rg, Town town, boolean notifyRegion, boolean notifyTown) {
        String townPvp = "";
        if (town.isTownPvp()) {
            if(town.isForcePvp())
            {
                townPvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("forced-pvp"));
            }
            else
            {
                townPvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("pvp"));
            }
        } else {
            townPvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("no-pvp"));
        }
        if (notifyRegion && !notifyTown) {
            ResidentRegion residentRegion = (ResidentRegion) rg;
            String rpvp = "";
            if (residentRegion.isPvp()) {
                rpvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("pvp"));
            } else {
                rpvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("no-pvp"));
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorCodes.toColor(AuroraUniverse.getLanguage().getString(Messages.Keys.REGION_WELCOME)
                    .replace("%s", residentRegion.getOwnerName()))
                    .replace("%p", rpvp)));
        }
        if (!notifyRegion && notifyTown) {

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-welcome").replace("%s", rg.getTown().getName()))
                    .replace("%p", townPvp)));

        }
        if (notifyRegion && notifyTown) {
            ResidentRegion residentRegion = (ResidentRegion) rg;
            String rpvp = "";
            if (residentRegion.isPvp()) {
                rpvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("pvp"));
            } else {
                rpvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("no-pvp"));
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorCodes.toColor(AuroraUniverse.getLanguage().getString(Messages.Keys.TOWN_REGION_WELCOME)
                    .replace("%town", rg.getTown().getName()))
                    .replace("%tpvp", townPvp)
                    .replace("%resident", residentRegion.getOwner().getName())
                    .replace("%rpvp", rpvp)));
        }
    }

    public static void handleChunkChange(Player player, ChunkPair ch) {
        if (AuroraUniverse.containsChunk((ch))) {
            //чанк городской
            Region rg = AuroraUniverse.getTownBlocks().get((ch));

            Resident resident = Residents.getResident(player);
            if (resident != null) {
                PlayerEnterTownEvent playerEnterTownEvent = new PlayerEnterTownEvent(rg.getTown(), resident);
                Town town = rg.getTown();

                ChunkPair lastChunk = resident.getLastChunk();

                resident.setLastChunk(ch);

                boolean notifyTown = false;
                boolean notifyRegion = false;

                if (resident.isLastWild()) {
                    // пришёл в город
                    Bukkit.getPluginManager().callEvent(playerEnterTownEvent);
                    resident.setLastwild(false);

                    resident.setLastTown(town.getName());
                    notifyTown = true;

                } else if (!rg.getTown().getName().equals(resident.getLastTown())) {
                    // перешёл в другой город
                    Bukkit.getPluginManager().callEvent(playerEnterTownEvent);
                    resident.setLastTown(rg.getTown().getName());
                    notifyTown = true;
                }
                if (lastChunk != null) {
                    Region region = AuroraUniverse.getTownBlock(lastChunk);
                    if (region != null) {
                        // пришёл из региона игрока

                        if (region.getTown() != town) {
                            notifyTown = true;
                        } else if (!(rg instanceof ResidentRegion) && region instanceof ResidentRegion) {
                            notifyTown = true;
                        }

                    }
                }

                if (rg instanceof ResidentRegion) {
                    notifyRegion = true;
                }

                if (rg instanceof OutpostRegion) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("outpost-welcome")).replace(
                            "%s", town.getName()).replace("%n", String.valueOf(town.getOutPosts().indexOf(rg)))
                    ));
                    return;
                }

                handleRegionChange(player, rg, town, notifyRegion, notifyTown);
            } else {
                Logger.warning("Can't find Resident with nickname " + player.getName());
            }
        } else {
            // чанк не городской
            Resident resident = Residents.getResident(player);
            if (resident != null) {
                if (!resident.isLastWild()) {
                    resident.setLastwild(true);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("world"))));
                }
            } else {
                Logger.warning("Can't find Resident with nickname " + player.getName());
            }
        }
    }

    public static boolean isNameValid(String name) {
        if (name.length() < 3) {
            return false;
        }
        if(!AuroraUniverse.matchesNameRegex(name)) return false;
        int maxLength = AuroraUniverse.getInstance().getConfig().getInt("max-town-name");
        if (name.length() > maxLength) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean createTown(String name, Player mayor) throws TownException {
        PreTownCreateEvent preTownCreateEvent = new PreTownCreateEvent(name.toString(), Residents.getResident(mayor), mayor.getLocation());
        Bukkit.getPluginManager().callEvent(preTownCreateEvent);

        if (!preTownCreateEvent.isCancelled()) {
            Town newtown = new Town(name, Residents.getResident(mayor), ChunkPair.fromChunk(mayor.getLocation().getChunk()));
            newtown.setSpawn(mayor.getLocation());
            Residents.getResident(mayor).setPermissionGroup("mayor");

            AuroraPermissions.setPermissions(mayor.getName(), AuroraPermissions.getGroup("mayor"));
            AuroraUniverse.getTownList().put(newtown.getName(), newtown);
            AuroraUniverse.getTownBlocks().putAll(newtown.getTownChunks());

            NewTownEvent newTownEvent = new NewTownEvent(newtown);
            Bukkit.getPluginManager().callEvent(newTownEvent);
            return true;
        }
        return false;
    }

    public static Town loadTown(String name, Resident mayor, ChunkPair mainChunk) throws TownException {


        Town newTown = new Town(name, mayor, mainChunk);

        mayor.setPermissionGroup("mayor");

        if (Bukkit.getOnlinePlayers().contains(mayor)) {
            AuroraPermissions.setPermissions(mayor.getName(), AuroraPermissions.getGroup("mayor"));
        }

        AuroraUniverse.getTownList().put(newTown.getName(), newTown);
        AuroraUniverse.getTownBlocks().putAll(newTown.getTownChunks());
        return newTown;


    }


}
