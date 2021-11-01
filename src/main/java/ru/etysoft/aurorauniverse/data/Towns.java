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
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.world.Region;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.Collection;

public class Towns {

    public static Town getTown(String name) {
        if (AuroraUniverse.townList != null) {
            if (AuroraUniverse.townList.containsKey(name)) {
                return AuroraUniverse.townList.get(name);
            } else {
                return null;
            }
        } else {
            Logger.warning("Townlist is null!");
            return null;
        }
    }


    public static boolean isTownExists(String s) {
        if (AuroraUniverse.townList.containsKey(s)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean hasMyTown(Chunk chunk, Town town) {
        if (AuroraUniverse.alltownblocks.containsKey(chunk)) {
            if (AuroraUniverse.alltownblocks.get(chunk).getTown() == town) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean hasTown(Chunk chunk) {
        if (AuroraUniverse.alltownblocks.containsKey(chunk)) {
            return true;
        } else {
            return false;
        }
    }

    public static Town getTown(Chunk chunk) {
        if (AuroraUniverse.alltownblocks.containsKey(chunk)) {
            return AuroraUniverse.alltownblocks.get(chunk).getTown();
        } else {
            return null;
        }
    }

    public static boolean hasTown(Location lc) {
        if (AuroraUniverse.alltownblocks.containsKey(lc.getChunk())) {
            return true;
        } else {
            return false;
        }
    }

    public static Collection<Town> getTowns()
    {
        return AuroraUniverse.townList.values();
    }


    public static void handleChunkChange(Player player, Chunk ch) {
        if (AuroraUniverse.alltownblocks.containsKey((ch))) {
            Region rg = AuroraUniverse.alltownblocks.get((ch));

            Resident resident = Residents.getResident(player);
            if (resident != null) {
                PlayerEnterTownEvent playerEnterTownEvent = new PlayerEnterTownEvent(rg.getTown(), resident);
                Town town = rg.getTown();
                String pvp = "";
                if(town.isPvp())
                {
                    pvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("pvp"));
                }
                else
                {
                    pvp = ColorCodes.toColor(AuroraUniverse.getLanguage().getString("no-pvp"));
                }
                if (resident.isLastWild()) {
                    Bukkit.getPluginManager().callEvent(playerEnterTownEvent);
                    resident.setLastwild(false);
                    resident.setLastTown(rg.getTown().getName());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-welcome").replace("%s", rg.getTown().getName()))
                    .replace("%p", pvp)));
                } else if (!rg.getTown().getName().equals(resident.getLastTown())) {
                    Bukkit.getPluginManager().callEvent(playerEnterTownEvent);
                    resident.setLastTown(rg.getTown().getName());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-welcome").replace("%s", rg.getTown().getName()))
                    .replace("%p", pvp)));
                }
            } else {
                Logger.warning("Can't find Resident with nickname " + player.getName());
            }
        } else {
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
            Town newtown = new Town(name, Residents.getResident(mayor), mayor.getLocation().getChunk());
            newtown.setSpawn(mayor.getLocation());
            Residents.getResident(mayor).setPermissionGroup("mayor");

            AuroraPermissions.setPermissions(mayor, AuroraPermissions.getGroup("mayor"));
            AuroraUniverse.getTownList().put(newtown.getName(), newtown);
            AuroraUniverse.getTownBlocks().putAll(newtown.getTownChunks());

            NewTownEvent newTownEvent = new NewTownEvent(newtown);
            Bukkit.getPluginManager().callEvent(newTownEvent);
            return true;
        }
        return false;
    }

    public static Town loadTown(String name, Resident mayor, Chunk mainChunk) throws TownException {


        Town newTown = new Town(name, mayor, mainChunk);

        mayor.setPermissionGroup("mayor");

        if (Bukkit.getOnlinePlayers().contains(mayor.getName())) {
            AuroraPermissions.setPermissions(Bukkit.getPlayer(mayor.getName()), AuroraPermissions.getGroup("mayor"));
        }

        AuroraUniverse.getTownList().put(newTown.getName(), newTown);
        AuroraUniverse.getTownBlocks().putAll(newTown.getTownChunks());
        return newTown;


    }


}
