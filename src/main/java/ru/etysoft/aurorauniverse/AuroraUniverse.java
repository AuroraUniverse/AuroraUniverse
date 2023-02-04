package ru.etysoft.aurorauniverse;


import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import ru.etysoft.aurorauniverse.chat.AuroraChat;
import ru.etysoft.aurorauniverse.commands.*;
import ru.etysoft.aurorauniverse.commands.nation.NationCommands;
import ru.etysoft.aurorauniverse.commands.nation.NationTabCompleter;
import ru.etysoft.aurorauniverse.data.Auction;
import ru.etysoft.aurorauniverse.data.DataManager;
import ru.etysoft.aurorauniverse.economy.AuroraEconomy;
import ru.etysoft.aurorauniverse.gulag.StalinNPC;
import ru.etysoft.aurorauniverse.listeners.PluginListener;
import ru.etysoft.aurorauniverse.listeners.ProtectionListener;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.placeholders.AuroraPlaceholdersExpansion;
import ru.etysoft.aurorauniverse.structures.StructurePatterns;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.LanguageSetup;
import ru.etysoft.aurorauniverse.utils.Metrics;
import ru.etysoft.aurorauniverse.utils.Timer;
import ru.etysoft.aurorauniverse.world.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class AuroraUniverse extends JavaPlugin {

    public static Map<String, Town> townList = new HashMap<>();
    public static Map<String, Nation> nationList = new HashMap<>();
    private static Map<ChunkPair, Region> alltownblocks = new HashMap<>();
    public static Map<String, Resident> residentlist = new HashMap<>();
    // public static int minTownBlockDistance = 1;
    public static boolean debugmode = true;

    private static String warnings = "";
    private static String prefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "AuroraUniverse" + ChatColor.GRAY + "]" + ChatColor.RESET;
    private AuroraEconomy auroraEconomy;
    private static AuroraUniverse instance;
    private File languagefile;
    private static FileConfiguration language;
    private static boolean haswarnings = false;

    private final static String langver = "0.1.1.6";
    private final static String confver = "0.1.0.4";
    private final static String permsver = "0.1.0.0";

    public AuroraEconomy getEconomy() {
        return auroraEconomy;
    }

    @Override
    public void onEnable() {
        Logger.info(">> &bAuroraUniverse &r" + getDescription().getVersion() + " by " + getDescription().getAuthors() + "<<");
        Timer timer = new Timer();
        instance = this;

        try {
            registerPlaceholders();
            Logger.info("Loading configuration...");
            saveDefaultConfig();
            String w1 = setupLanguageFile();
            if (!w1.equals("ok")) {
                addWarning(w1);
            }
            prefix = AuroraLanguage.getColorString("prefix");
            if (getConfig().contains("file-version")) {
                try {
                    if (!getConfig().getString("file-version").equals(confver)) {
                        addWarning("&eOutdated configuration file!");
                    }
                    if (language.contains("file-version")) {
                        if (!AuroraLanguage.getColorString("file-version").equals(langver)) {
                            addWarning("&eOutdated language file!");
                        }
                    } else {
                        addWarning("&eCan't find file-version in language file! You can add it manually with new params and plugin version(" + getDescription().getVersion() + ")");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    addWarning("&eERROR: " + e.getMessage());
                }
            } else {
                addWarning("&eCan't find file-version in config.yml!");
            }


            if (getConfig().getBoolean("use-aurora-chat")) {
                Logger.info("Initializing AuroraChat...");
                try {
                    AuroraChat.initialize();
                } catch (Exception e) {
                    addWarning("&cChat initializing issues occurred!");
                }
            }

            Logger.info("Loading structure patterns...");
            try {
                StructurePatterns.loadPatterns();
            } catch (Exception e) {
                e.printStackTrace();
                addWarning("&cStructure patterns load issues occurred!");
            }

            if (haswarnings) {
                Logger.info("&eConfiguration loaded with warnings.");
            }
        } catch (Exception e) {
            addWarning("&cCONFIGURATION ERROR(Probably language file is outdated): " + e.getMessage());
            if (AuroraLanguage.getDebugMode()) {
                Logger.debug("Configuration error: ");
                e.printStackTrace();
            }
        }

        // LISTENERS
        Logger.info("Initializing metrics...");
        try {
            new Metrics(this, 13258);
        } catch (Exception e) {
            addWarning("&METRICS ERROR: " + e.getMessage());
        }
        Logger.info("Initializing listeners and commands...");
        try {
            registerListeners();
            registerCommands();
        } catch (Exception e) {
            addWarning("&cLISTENERS ERROR: " + e.getMessage());
        }
        Logger.info("Initializing AuroraPermissions...");
        try {
            AuroraPermissions.initialize();
        } catch (Exception e) {
            addWarning("&cAPERMS ERROR: " + e.getMessage());
        }

        Logger.info("Initializing WorldTimer...");
        WorldTimer.getInstance();

        Logger.info("Initializing AuroraEconomy...");
        try {
            auroraEconomy = new AuroraEconomy();
        } catch (Exception e) {
            if (AuroraLanguage.getDebugMode()) {
                Logger.fatalError("Can't create EconomyCore:");
                e.printStackTrace();
            }
        }
        if (!setupEconomy()) {
            addWarning("Can't find Vault! Economy can't start!");
            return;
        }

        Logger.info("Loading data...");
        if (!DataManager.loadData()) {
            addWarning("&cAn error occurred loading data from json!");
        }

        Logger.info("Loading auction...");
        if (!Auction.loadListings()) {
            addWarning("&cAn error occurred loading auction data from json!");
        }
        int maxBound = 4;
        String seconds = timer.getStringSeconds();

        if (seconds.length() < 4) {
            maxBound = seconds.length() - 1;
        }

        if (!haswarnings) {
            //If no warnings
            Logger.info("AuroraUniverse successfully enabled in " + seconds.substring(0, maxBound) + " seconds!");
        } else {
            //Some warnings catched
            Logger.info("&cAuroraUniverse enabled with warnings in " + seconds.substring(0, maxBound) + " seconds: &e" + warnings);
        }
        if (AuroraLanguage.getDebugMode()) {
            Logger.debug("You running AuroraUniverse in debug mode (more console messages)");
        }

    }

    public static int getMinTownsDistance() {
        int value = AuroraUniverse.getInstance().getConfig().getInt("min-distance-between-towns");

        if (value >= 1) {
            return value;
        }
        Logger.warning("Wrong min-distance-between-towns value: " + value);
        return 1;
    }

    public static int getMaxOutposts() {
        int value = AuroraUniverse.getInstance().getConfig().getInt("max-outposts-count");

        if (value >= 0) {
            return value;
        }
        Logger.warning("Wrong max-outposts-count value: " + value);
        return 1;
    }

    public static boolean matchesStringRegex(String toMatch) {
        String regex = AuroraUniverse.getInstance().getConfig().getString("string-regex");
        try {
            Pattern.compile(regex);
        } catch (Exception e) {
            Logger.error("String regex is incorrect!");
            return false;
        }
        return Pattern.matches(regex, toMatch);
    }


    public static boolean matchesNameRegex(String toMatch) {
        String regex = AuroraUniverse.getInstance().getConfig().getString("name-regex");
        try {
            Pattern.compile(regex);
        } catch (Exception e) {
            Logger.error("Name regex is incorrect!");
            return false;
        }
        return Pattern.matches(regex, toMatch);
    }


    public String setupLanguageFile() {
        boolean ok = true;
        languagefile = new File(AuroraUniverse.getInstance().getDataFolder(), AuroraUniverse.getInstance().getConfig().getString("language-file"));
        if (!languagefile.exists()) {
            try {
                languagefile.getParentFile().mkdirs();
                AuroraUniverse.getInstance().saveResource(AuroraUniverse.getInstance().getConfig().getString("language-file"), false);
            } catch (Exception e) {
                AuroraUniverse.getInstance().saveResource("english.yml", false);
                languagefile = new File(AuroraUniverse.getInstance().getDataFolder(), "english.yml");
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

        if (ok) {
            return "ok";
        } else {
            File file2 = new File(AuroraUniverse.getInstance().getDataFolder(), AuroraUniverse.getInstance().getConfig().getString("language-file"));

            try {
                Files.copy(AuroraUniverse.getInstance().getResource("english.yml"), Paths.get(file2.getAbsolutePath()));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return "Looks like you tried to use new language file, but that file doesn't exists. Using english.yml";
        }

    }

    public static String getWarnings() {
        return warnings;
    }


    private void addWarning(String s) {
        haswarnings = true;
        warnings += "\n" + s;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static Map<String, Town> getTownList() {
        return townList;
    }

    public static Map<String, Nation> getNationList() {
        return nationList;
    }

    @Deprecated
    public static Map<ChunkPair, Region> getTownBlocks() {
        return alltownblocks;
    }


    public static Region getTownBlock(ChunkPair chunkPair) {
        return alltownblocks.get(chunkPair);
    }

    public static void addTownBlock(ChunkPair chunkPair, Region region) {
        alltownblocks.put(chunkPair, region);
    }

    public static void removeTownBlock(ChunkPair chunkPair) {
        alltownblocks.remove(chunkPair);
    }

    public static boolean containsChunk(ChunkPair chunkPair) {
        return alltownblocks.containsKey(chunkPair);
    }

    public static AuroraUniverse getInstance() {
        return instance;
    }

    public Economy getVaultEconomy() {
        if(instance.isUsingAuroraEconomy()) return null;
        return getServer().getServicesManager().getRegistration(Economy.class).getProvider();
    }

    public static FileConfiguration getLanguage() {
        return language;
    }

    private void registerPlaceholders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AuroraPlaceholdersExpansion(this).register();
        }
    }

    private void registerCommands() {
        registerCommand("auntown", new TownCommands(), new TownTabCompleter());
        registerCommand("aurorauniverse", new PluginCommands(), new MainTabCompleter());
        registerCommand("auneco", new EconomyCommands(), new EconomyTabCompleter());
        registerCommand("aunnation", new NationCommands(), new NationTabCompleter());
        registerCommand("aunchat", AuroraChat.getInstance().getChatCommand(), new ChatTabCompleter());
        registerCommand("aunauction", new AuctionCommands(), new AuctionTabCompleter());

    }

    private void registerListeners() {
        registerListener(new PluginListener());
        registerListener(new ProtectionListener());
    }


    private boolean registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        try {
            PluginCommand command = getCommand(name);
            command.setExecutor(executor);
            if (tabCompleter != null) {
                command.setTabCompleter(tabCompleter);
            }
            if (AuroraLanguage.getDebugMode()) {
                Logger.debug("Registered command &b/" + name);
            }
            return true;
        } catch (Exception e) {
            Logger.error("Can't register /" + name + " command!");
            return false;
        }
    }

    public static void registerListener(org.bukkit.event.Listener listener) {
        if (AuroraLanguage.getDebugMode()) {
            Logger.debug("Registered listener &b" + listener.getClass().getSimpleName());
        }
        getInstance().getServer().getPluginManager().registerEvents(listener, getInstance());
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        getServer().getServicesManager().register(Economy.class, auroraEconomy, this, ServicePriority.Highest);
        Logger.info("Economy has been registered.");
        return true;
    }

    public boolean isUsingAuroraEconomy() {
        return getConfig().getBoolean("use-aurora-economy");
    }
    public boolean isAuctionEnabled() {

        return getConfig().getBoolean("auction-enabled");
    }

    public boolean isAuctionStructureEnabled() {
        return getConfig().getBoolean("auction-struct-enabled");
    }

    //Disabling
    @Override
    public void onDisable() {
        Logger.info("Disabling AuroraUniverse...");
        DataManager.saveData();
        Auction.saveListings();
        try {
            StalinNPC.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.info("AuroraUniverse successfully disabled!");
    }
}
