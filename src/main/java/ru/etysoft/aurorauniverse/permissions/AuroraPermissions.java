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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuroraPermissions {
    private static Map<String, Group> groups = new ConcurrentHashMap<>();
    private static boolean isInitialized = false;
    private static HashMap<UUID, PermissionAttachment> permissionAttachments = new HashMap<>();


    public static void clear() {
        permissionAttachments.forEach((uuid, attachment) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.removeAttachment(attachment);
            } else {
                Logger.debug("Player " + player.getName() + " not found!");
            }
        });
        groups.clear();
        permissionAttachments.clear();
    }

    public static void initialize() {
        try {
            YamlConfiguration pemissionsfile = FileManager.setupYaml("permissions.yml");
            if (pemissionsfile != null) {

                for (String groupName :
                        pemissionsfile.getConfigurationSection("groups.town").getKeys(false)) {
                    Group group = new Group(groupName, pemissionsfile.getStringList("groups.town." + groupName));
                    addGroup(group);
                }
                Group group = new Group("newbies", pemissionsfile.getStringList("newbies"));
                addGroup(group);
                parsePlayers();
                isInitialized = true;
            } else {
                Logger.debug("Permissions file is null!");
            }
        } catch (YamlException e) {
            Logger.error("Can't initialize permissions file!");
            e.printStackTrace();
        }
    }

    public static void parsePlayers() {
        AuroraUniverse.residentlist.forEach((name, resident) -> {
            Player player = Bukkit.getPlayer(name);
            Group group = getGroup(resident.getPermissonGroupName());
            setPermissons(player, group);
        });
    }

    public static void setPermissons(Player player, Group group) {
        PermissionAttachment attachment = player.addAttachment(AuroraUniverse.getInstance());
        if (permissionAttachments.containsKey(player.getUniqueId())) {
            Logger.debug("Remove permissions from &b" + player.getName());
            player.removeAttachment(permissionAttachments.get(player.getUniqueId()));
            permissionAttachments.remove(player.getUniqueId());
        }

        Logger.debug("Attaching permissions of " + group.getName() + " to " + player.getName());
        Residents.getResident(player.getName()).setPermissonGroup(group.getName());
        for (String permission :
                group.getPermissions()) {
            attachment.setPermission(permission, !permission.startsWith("-"));
            Logger.debug("Set permisson " + permission);
        }
        permissionAttachments.put(player.getUniqueId(), attachment);
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

    public static void removePermissons(Player player) {
        permissionAttachments.remove(player.getUniqueId());
    }

    public static void addGroup(Group group) {
        Logger.debug("Added " + group.getName() + " permission group...");
        if (!groups.containsKey(group.getName())) {
            groups.put(group.getName(), group);
        }
    }

    public static Group getGroup(String name) {
        return groups.get(name);
    }

    public static boolean hasGroup(String groupName) {
        if (isInitialized) {
            return groups.containsKey(groupName);
        }
        return false;
    }


}
