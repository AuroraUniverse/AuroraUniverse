package ru.etysoft.aurorauniverse.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.exceptions.YamlException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {

    public static YamlConfiguration setupYaml(String filename) throws YamlException {
        boolean ok = true;
        File file = new File(AuroraUniverse.getInstance().getDataFolder(), filename);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                AuroraUniverse.getInstance().saveResource(filename, false);
            } catch (Exception e) {
                Logger.error("Can't create " + filename + ":");
                e.printStackTrace();
                AuroraUniverse.getInstance().saveResource(filename, false);
                file = new File(AuroraUniverse.getInstance().getDataFolder(), filename);
                file.getParentFile().mkdirs();
                ok = false;
            }
        }

        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }


        if (ok) {
            return yamlConfiguration;
        } else {
            File file2 = new File(AuroraUniverse.getInstance().getDataFolder(), AuroraUniverse.getInstance().getConfig().getString("language-file"));

            try {
                Files.copy(AuroraUniverse.getInstance().getResource(filename), Paths.get(file2.getAbsolutePath()));
            } catch (Exception e2) {
                Logger.error("Can't copy " + filename + ":");
                e2.printStackTrace();
                e2.printStackTrace();
                throw new YamlException(e2);
            }

        }
        return null;
    }

}
