package ru.etysoft.aurorauniverse;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AuroraUniverse extends JavaPlugin {

    @Override
    public void onEnable() {
        Logger.info(">> &bAuroraUniverse &r" + getDescription().getVersion() + " by " + getDescription().getAuthors() + "<<");
        boolean haswarnings = false;
        String warnings = "";
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
