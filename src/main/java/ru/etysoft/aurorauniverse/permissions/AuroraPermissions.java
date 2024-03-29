package ru.etysoft.aurorauniverse.permissions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.YamlException;
import ru.etysoft.aurorauniverse.utils.FileManager;
import ru.etysoft.aurorauniverse.world.Resident;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuroraPermissions {
    private static Map<String, Group> groups = new HashMap<>();
    private static boolean isInitialized = false;
    private static HashMap<UUID, PermissionAttachment> permissionDictionary = new HashMap<>();


    public static void clear() {
        for(UUID uuid : permissionDictionary.keySet())
        {
            PermissionAttachment permissionAttachment = permissionDictionary.get(uuid);
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.removeAttachment(permissionAttachment);
            } else {
                Logger.debug("Player " + " not found!");
            }
        }

        groups.clear();
        permissionDictionary.clear();
    }

    public static void initialize() {
        try {
            YamlConfiguration premissionsYml = FileManager.setupYaml("permissions.yml");
            if (premissionsYml != null) {

                for (String groupName :
                        premissionsYml.getConfigurationSection("groups.town").getKeys(false)) {
                    Group group = new Group(groupName, premissionsYml.getStringList("groups.town." + groupName));
                    addGroup(group);
                }
                Group group = new Group("newbies", premissionsYml.getStringList("newbies"));
                addGroup(group);
                parsePlayers();
                isInitialized = true;
            } else {
                Logger.error("Permissions file is null!");
            }
        } catch (YamlException e) {
            Logger.error("Can't initialize permissions file!");
            e.printStackTrace();
        }
    }

    public static void parsePlayers() {

        for(Player player : Bukkit.getOnlinePlayers())
        {
            Group group = AuroraPermissions.getGroup(Residents.getResident(player).getPermissionGroupName());
            AuroraPermissions.setPermissions(player.getName(), group);
        }

    }

    public static void setPermissions(String nickname, Group group) {

        Player player = Bukkit.getPlayer(nickname);

        Resident resident = Residents.getResident(nickname);
        if(resident != null) {
            resident.setPermissionGroup(group.getName());
        }

        if(player != null) {
            PermissionAttachment attachment = player.addAttachment(AuroraUniverse.getInstance());
            if (permissionDictionary.containsKey(player.getUniqueId())) {
                Logger.debug("Remove permissions from &b" + player.getName());
                player.removeAttachment(permissionDictionary.get(player.getUniqueId()));
                permissionDictionary.remove(player.getUniqueId());
            }

            Logger.debug("Attaching permissions of " + group.getName() + " to " + player.getName());
            Residents.getResident(player.getName()).setPermissionGroup(group.getName());
            for (String permission :
                    group.getPermissions()) {
                attachment.setPermission(permission, !permission.startsWith("-"));
                Logger.debug("Set permission " + permission);
            }
            permissionDictionary.put(player.getUniqueId(), attachment);


        }
        else
        {
            Logger.debug("[Permissions] Player " + nickname + " is null!");
        }
    }

    public static Map<String, Group> getGroups() {
        if (isInitialized) {
            return groups;
        }
        return null;
    }

    public static void removeGroup(String groupName) {
        if (isInitialized) {
            groups.remove(groupName);
        }
    }

    public static void removePermissions(Player player) {
        permissionDictionary.remove(player.getUniqueId());
    }

    public static void addGroup(Group group) {

        if (!groups.containsKey(group.getName())) {
            Logger.info("Added " + group.getName() + " permission group...");
            groups.put(group.getName(), group);
        }
        else
        {
            Logger.error("Group " + group.getName() + " already loaded!");
        }
    }

    public static Group getGroup(String name) {
        if(!groups.containsKey(name))
        {
            Logger.error("Group " + name + " not found!");
        }
        return groups.get(name);
    }

    public static boolean hasGroup(String groupName) {
        if (isInitialized) {
            return groups.containsKey(groupName);
        }
        return false;
    }


}
