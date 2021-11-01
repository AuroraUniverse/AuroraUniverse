package ru.etysoft.aurorauniverse.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.world.Resident;

import java.util.concurrent.ExecutionException;

public class AuroraPlaceholdersExpansion extends PlaceholderExpansion {

    private AuroraUniverse plugin;

    public AuroraPlaceholdersExpansion(AuroraUniverse plugin) {
        this.plugin = AuroraUniverse.getInstance();
    }

    @Override
    public String getAuthor() {
        return "karlov_m";
    }

    @Override
    public String getIdentifier() {
        return "aurorauniverse";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }


    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

//        if(params.equalsIgnoreCase("name")) {
//            return player == null ? null : player.getName(); // "name" requires the player to be valid
//        }
//
//        if(params.equalsIgnoreCase("placeholder1")) {
//            return "Placeholder Text 1";
//        }
//
//        if(params.equalsIgnoreCase("placeholder2")) {
//            return "Placeholder Text 2";
//        }

        if(params.equalsIgnoreCase("town"))
        {
            if(Residents.getResident(player.getName()) != null)
            {
                Resident resident = Residents.getResident(player.getName());
                try
                {
                    return AuroraLanguage.getColorString("placeholders.town").replace("%s", resident.getTown().getName());
                }
                catch (TownNotFoundedException ignored)
                {
                    return AuroraLanguage.getColorString("placeholders.no-town");
                }
            }
            Residents.createResident(player.getName());
            return ColorCodes.toColor("&c[NULL RES] &r");
        }
        else if(params.equalsIgnoreCase("nation"))
        {
            if(Residents.getResident(player.getName()) != null)
            {
                Resident resident = Residents.getResident(player.getName());
                try
                {
                    if(resident.getTown().getNation() != null)
                    {
                        return AuroraLanguage.getColorString("placeholders.nation").replace("%s", resident.getTown().getNation().getName());
                    }
                    else
                    {
                        return AuroraLanguage.getColorString("placeholders.no-nation");
                    }

                }
                catch (TownNotFoundedException ignored)
                {
                    return AuroraLanguage.getColorString("placeholders.no-nation");
                }
            }
            return ColorCodes.toColor("&c[AUN ERROR]");
        }
        return null;
        // Placeholder is unknown by the Expansion
    }
}