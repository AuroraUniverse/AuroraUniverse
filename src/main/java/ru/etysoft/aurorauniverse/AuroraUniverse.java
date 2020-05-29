package ru.etysoft.aurorauniverse;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import ru.etysoft.aurorauniverse.economy.EconomyCore;
import ru.etysoft.aurorauniverse.exceptions.TownException;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AuroraUniverse extends JavaPlugin {

    public static String warnings = "";
    public static Map<String, Town> townlist = new ConcurrentHashMap<>();
    public static Map<Chunk, Region> alltownblocks = new ConcurrentHashMap<>();
    public static Map<String, Resident> residentlist = new ConcurrentHashMap<>();
    public static int minTownBlockDistanse = 1;
    public EconomyCore economyCore;

    public EconomyCore getEconomy()
    {
        return  economyCore;
    }

    @Override
    public void onEnable() {
        Logger.info(">> &bAuroraUniverse &r" + getDescription().getVersion() + " by " + getDescription().getAuthors() + "<<");
        boolean haswarnings = false;

        try {
            //Utilities for warning system


            //CONFIGURATION LOAD
            Logger.info("Loading configuration...");

            saveDefaultConfig();
            String w1 = createLanguageFile();
            if (!w1.equals("ok")) {
                warnings = warnings + "\n" + w1;
                haswarnings = true;
            }
            prefix = fun.cstring(language.getString("prefix"));
            if (getConfig().contains("file-version")) {
                try {
                    if (!getConfig().getString("file-version").equals(this.getDescription().getVersion())) {

                        warnings = warnings + "\n&eOutdated configuration file!";
                        haswarnings = true;
                    }
                    if (language.contains("file-version")) {


                        if (!language.getString("file-version").equals(this.getDescription().getVersion())) {
                            warnings = warnings + "\n&eOutdated language file!";
                            haswarnings = true;
                        }
                    } else {
                        warnings = warnings + "\n&eCan't find file-version in language file! You can add it manually with new params and plugin version(" + getDescription().getVersion() + ")";
                        haswarnings = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    warnings = warnings + "\n&eERROR: " + e.getMessage();
                    haswarnings = true;
                }
            } else {
                warnings = warnings + "\n&eCan't find file-version in config.yml!";
                haswarnings = true;
            }
            if (haswarnings) {
                Logger.info("&eConfiguration loaded with warnings.");
            }
        }
        catch (Exception e)
        {
            warnings = warnings + "\n&cCONFIGURATION ERROR(Probably language file is outdated): " + e.getMessage();
            haswarnings = true;
        }

        // LISTENERS
        Logger.info("Initializing listeners...");
        try
        {
            getServer().getPluginManager().registerEvents(new Listener(), this);
        }
        catch (Exception e)
        {
            warnings = warnings + "\n&cLISTENERS ERROR: " + e.getMessage();
            haswarnings = true;
        }
        try
        {
            economyCore = new EconomyCore();

        }
        catch (Exception e){}
        // ECONOMY
        if (!setupEconomy()) {

            warnings = warnings + "\nCan't find Vault! Economy can't start!.";
            return;
        }
        else
        {

        }


       if(!haswarnings)
       {
           //If no warnings
           Logger.info("AuroraUniverse successfully enabled!");
       }
       else
       {
           //Some warnings catched
           Logger.info("&cAuroraUniverse was enabled with warnings: &e" + warnings);
       }

    }

    public static FileConfiguration getLanguage() {
        return language;
    }

    private File languagefile;
    private static FileConfiguration language;
    private String createLanguageFile() {
      boolean ok = true;
        languagefile = new File(getDataFolder(), getConfig().getString("language-file"));
        if (!languagefile.exists()) {
            try
            {
                languagefile.getParentFile().mkdirs();
                saveResource(getConfig().getString("language-file"), false);
            }
           catch (Exception e)
           {
               saveResource("english.yml", false);
               languagefile = new File(getDataFolder(), "english.yml");
               languagefile.getParentFile().mkdirs();






              ok = false;
           }
        }

        language = new YamlConfiguration();
        try {
           language.load(languagefile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        LanguageSetup.setup(language);

        if(ok)
        {
            return "ok";
        }
        else
        {
            File file2 = new File(getDataFolder(), getConfig().getString("language-file"));

            try
            {
                Files.copy(getResource("english.yml"), Paths.get(file2.getAbsolutePath()));
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
            return "Looks like you tried to use new language file, but that file doesn't exists. Using english.yml";
        }

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        getServer().getServicesManager().register(Economy.class, economyCore, this, ServicePriority.Highest);
        Logger.info("Economy has been registered.");
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /** AUN
         * Main
         * Plugin Command
         **/
        if(label.equalsIgnoreCase("aun"))
        {

            if(args.length > 0)
            {
                //Has arguments

                //RELOAD COMMAND
                if(args[0].equalsIgnoreCase("reload"))
                {
                    if(sender.hasPermission("aun.admin"))
                    {
                        reloadConfig();
                        onEnable();
                        Messaging.mess(language.getString("reloaded-message"), sender);

                    }
                    else
                    {
                        //Haven't permission
                        Messaging.mess(language.getString("access-denied-message"), sender);
                        Logger.info(sender.getName() + " tried to use RELOAD command. Access denied.");
                    }


                    return  true;
                }

            }
            else
            {
                //No arguments message
              Messaging.plinfo(sender, this);
                return  true;
            }

        }
        if(label.equalsIgnoreCase("t")) {
            if(args.length > 0) {
                if (args[0].equalsIgnoreCase("new")) {
                    if (args.length > 1) {
                        try {
                            Player pl = (Player) sender;
                            StringBuilder name = new StringBuilder();
                            for (String arg :
                                    args) {
                                if (!arg.equals(args[0])) {
                                    name.append(arg).append(" ");
                                }
                            }
                            try {
                                Town newtown = new Town(name.toString(), TownFun.getResident(pl), pl.getLocation().getChunk());
                                newtown.townspawn = pl.getLocation();
                                townlist.put(newtown.name, newtown);

                                alltownblocks.putAll(newtown.getTownChunks());
                                Messaging.mess(language.getString("town-created-message").replace("%s", name), sender);

                                TownFun.getResident(pl).lastwild = false;
                            } catch (TownException e) {
                                Messaging.mess(language.getString("town-cantcreate-message").replace("%s", e.getMessageErr()), pl);
                            }
                        } catch (Exception e)
                        {

                            if(!(sender instanceof Player))
                            {
                                Town newtown = new Town();


                                townlist.put(args[1], newtown);
                            }
                            else
                            {
                                Messaging.mess("Error!", sender);
                            }
                        }
                    } else {
                        // TODO: no argument message
                        Messaging.mess(language.getString("no-arguments"), sender);
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("delete")) {

                            Player pl = (Player) sender;

                            try {
                              Resident resident = TownFun.getResident(pl);
                                assert resident != null;
                                if(resident.getTown().isMayor(resident))
                               {
                                   if(resident.getTown().delete())
                                   {
                                       Messaging.mess(fun.cstring(language.getString("town-deleted-message")), pl);
                                   }

                               }
                                else
                                {
                                    Messaging.mess(language.getString("access-denied-message"), sender);
                                }
                            }
                            catch (Exception e)
                            {
                                Messaging.mess(language.getString("town-cantcreate-message").replace("%s", e.getMessage()), pl);
                            }

                        return true;
                } else if (args[0].equalsIgnoreCase("spawn")) {
                    Player pl = (Player) sender;
                    Resident resident = TownFun.getResident(pl);

                    if (resident.hasTown()) {
                        Town t = resident.getTown();
                        t.spawnplayer(pl);
                        Messaging.mess(language.getString("town-teleported-to-spawn"), sender);
                    } else {
                        //TODO: resident don't belong to
                        Messaging.mess(language.getString("town-dont-belong"), sender);
                    }


                    return true;
                } else if (args[0].equalsIgnoreCase("list")) {
                    Player pl = (Player) sender;
                    pl.sendMessage(fun.cstring(language.getString("town-list")));
                    int page = 1;
                    if(args.length > 1)
                    {
                        try
                        {
                            page = Integer.parseInt(args[1]);
                        }
                       catch (Exception e)
                       {
                           Messaging.mess(language.getString("no-arguments"), sender);
                       }
                    }
                    final int[] i = {1};
                    int finalPage = page;
                   double d = Double.parseDouble("" + townlist.size());
                    double maxPage = Math.ceil((double) d / 10f);
                    townlist.forEach((name, town) -> {

                       if(i[0] != (10 * finalPage) + 1)
                       {
                           if(i[0] > maxPage - 1) {
                               try
                               {
                                   pl.sendMessage(ChatColor.AQUA + name + ChatColor.GOLD + "(" + town.getMembersCount() + ", " + town.getMayor().getName() + ")");
                               }
                               catch (Exception e)
                               {
                                   e.printStackTrace();
                                   pl.sendMessage(name);
                               }




                           }
                       }
                        i[0]++;
                   });

                  Messaging.mess(language.getString("town-pages").replace("%s", String.valueOf(page)).replace("%y", townlist.size() + ""), pl);

                    return true;
                } else if (args[0].equalsIgnoreCase("claim")) {
                    Player pl = (Player) sender;
                    Resident resident = TownFun.getResident(pl);
                    if (resident.hasTown()) {
                        Town t = resident.getTown();
                        if(t.getMayor() == resident) {
                            try {
                                if(t.claimChunk(pl.getLocation().getChunk()))
                                {
                                    Messaging.mess(language.getString("town-claim"), sender);
                                    resident.lastwild = false;
                                    resident.setLastTown(t.getName());
                                }
                                else
                                {
                                    Messaging.mess(language.getString("town-cantclaim"), sender);
                                }
                            } catch (TownException e) {
                                e.printStackTrace();
                            }
                        }
                        // TODO: permissions claim
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("unclaim")) {
                    Player pl = (Player) sender;
                    Resident resident = TownFun.getResident(pl);
                    if (resident.hasTown()) {
                        Town t = resident.getTown();
                        if(t.getMayor() == resident) {

                                if(t.unclaimChunk(pl.getLocation().getChunk()))
                                {
                                    Messaging.mess(language.getString("town-unclaim"), sender);
                                    resident.lastwild = true;
                                    TownFun.ChangeChunk(pl, pl.getLocation().getChunk());
                                }
                                else
                                {
                                    Messaging.mess(language.getString("town-cantunclaim"), sender);
                                }

                        }
                        // TODO: permissions claim
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("leave")) {
                    Player pl = (Player) sender;
                    Resident resident = TownFun.getResident(pl);
                    if (resident.hasTown()) {
                        Town t = resident.getTown();
                        t.removeResident(resident);
                        Messaging.mess(language.getString("town-leave").replace("%s", t.getName()), pl);

                        // TODO: permissions claim
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if(args.length > 1)
                    {
                        Player pl = (Player) sender;
                        Resident resident = TownFun.getResident(pl);
                        if (resident.hasTown()) {
                            Town t = resident.getTown();
                            t.removeResident(resident);
                            double d = 0;
                            try {
                                d = Double.valueOf(args[1]);

                            } catch (Exception e)
                            {
                                Messaging.mess(language.getString("no-arguments"), sender);
                                return true;
                            }
                            if(resident.takeBalance(d))
                            {
                                EconomyResponse r = economyCore.bankDeposit(t.getName(), d);
                                if(r.type == EconomyResponse.ResponseType.SUCCESS)
                                {
                                    Messaging.mess(language.getString("town-deposit").replace("%s", d + ""), pl);

                                }
                                else
                                {
                                    Messaging.mess("Deposit error: " + r.errorMessage, pl);
                                }
                            }
                           else
                            {
                                Messaging.mess(language.getString("town-cantdeposit").replace("%s", d + ""), pl);
                            }
                            // TODO: permissions claim
                        }
                        else
                        {
                            Messaging.mess(language.getString("town-dont-belong"), sender);
                        }
                    }
                    else
                    {
                        Messaging.mess(language.getString("no-arguments"), sender);
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if(args.length > 1)
                    {
                        Player pl = (Player) sender;
                        Resident resident = TownFun.getResident(pl);
                        if (resident.hasTown()) {
                            Town t = resident.getTown();
                            t.removeResident(resident);
                            double d = 0;
                            try {
                                d = Double.valueOf(args[1]);

                            } catch (Exception e)
                            {
                                Messaging.mess(language.getString("no-arguments"), sender);
                                return true;
                            }
                            EconomyResponse r = economyCore.bankWithdraw(t.getName(), d);
                            if(r.type == EconomyResponse.ResponseType.SUCCESS)
                            {
                                Messaging.mess(language.getString("town-withdraw").replace("%s", d + ""), pl);
                                resident.giveBalance(d);
                            }
                            else
                            {
                                Messaging.mess(language.getString("town-cantwithdraw").replace("%s", d + ""), pl);
                            }
                            // TODO: permissions claim
                        }
                        else
                        {
                            Messaging.mess(language.getString("town-dont-belong"), sender);
                        }
                    }
                    else
                    {
                        Messaging.mess(language.getString("no-arguments"), sender);
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("spawn")) {
                            Player pl = (Player) sender;
                            Resident resident = TownFun.getResident(pl);

                            if (resident.hasTown()) {
                                Town t = resident.getTown();
                                if (t.getMayor() == resident) {
                                    // TODO: permissions set
                                    try {
                                        t.setSpawn(pl.getLocation());
                                        Messaging.mess(language.getString("town-setspawn"), pl);
                                    } catch (TownException e) {
                                        Messaging.mess(language.getString("town-cantsetspawn").replace("%s", e.getMessageErr()), pl);

                                    }

                                } else {
                                    Messaging.mess(language.getString("access-denied-message"), pl);
                                }
                            } else {
                                //TODO: resident don't belong to town
                                Messaging.mess(language.getString("town-dont-belong"), sender);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length > 1) {

                            Player de = (Player) Bukkit.getPlayer(args[1]);
                            Resident resident2 = TownFun.getResident(de);
                        Player pl = (Player) sender;
                        Resident resident = TownFun.getResident(pl);
                            if (resident.hasTown() && resident2 != null) {
                                Town t = resident.getTown();
                                if (t.getMayor() == resident) {
                                    // TODO: permissions set
                                    t.removeResident(resident2);
                                    Messaging.mess(language.getString("town-kick").replace("%s", resident2.getName()), pl);


                                } else {
                                    Messaging.mess(language.getString("access-denied-message"), pl);
                                }
                            } else {
                                //TODO: resident don't belong to town
                                Messaging.mess(language.getString("town-dont-belong"), sender);
                            }
                        }
                    }
              else if (args[0].equalsIgnoreCase("add")) {
                if (args.length > 1) {

                    Player de = (Player) Bukkit.getPlayer(args[1]);
                    Resident resident2 = TownFun.getResident(de);
                    Player pl = (Player) sender;
                    Resident resident = TownFun.getResident(pl);
                    if (resident.hasTown() && resident2 != null) {
                        Town t = resident.getTown();
                        if (t.getMayor() == resident) {
                            // TODO: permissions set
                            t.addResident(resident2);
                            Messaging.mess(language.getString("town-invite").replace("%s", resident2.getName()), pl);

                        } else {
                            Messaging.mess(language.getString("access-denied-message"), pl);
                        }
                    } else {
                        //TODO: resident don't belong to town
                        Messaging.mess(language.getString("town-dont-belong"), sender);
                    }
                }

                    } else if (args[0].equalsIgnoreCase("toggle")) {
                        if (args.length > 2) {
                            if (args[1].equalsIgnoreCase("pvp")) {
                                Player pl = (Player) sender;
                                Resident resident = TownFun.getResident(pl);

                                if (resident.hasTown()) {
                                    Town t = resident.getTown();
                                    if (t.getMayor() == resident) {
                                        // TODO: permissions set
                                        if(args[2].equals("on"))
                                        {
                                            t.setPvP(true);
                                            Messaging.mess(language.getString("town-pvpon"), pl);
                                        }
                                        else if(args[2].equals("off"))
                                        {
                                            t.setPvP(false);
                                            Messaging.mess(language.getString("town-pvpoff"), pl);
                                        }



                                    } else {
                                        Messaging.mess(language.getString("access-denied-message"), pl);
                                    }
                                } else {
                                    //TODO: resident don't belong to town
                                    Messaging.mess(language.getString("town-dont-belong"), sender);
                                }
                            }
                    } else {
                        // TODO: no argument message
                            Messaging.mess(language.getString("no-arguments"), sender);
                    }


                    return true;
                }
              else
                {
                    Messaging.mess(language.getString("no-arguments"), sender);
                }
            }
            else
            {
                // TODO: town
                Player pl = (Player) sender;
                Resident resident = TownFun.getResident(pl);
                if(resident.hasTown())
                {
                    Messaging.towninfo(sender, resident.getTown());
                }
                else
                {
                    //TODO: no town message
                    Messaging.mess(language.getString("town-dont-belong"), sender);
                }

            }
            return true;
        }

        return false;
    }

    public static String prefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "AuroraUniverse" + ChatColor.GRAY +"]" + ChatColor.RESET;

    //Disabling
    @Override
    public void onDisable() {
        Logger.info("Disabling AuroraUniverse...");
        Logger.info("AuroraUniverse successfully disabled!");
    }
}
