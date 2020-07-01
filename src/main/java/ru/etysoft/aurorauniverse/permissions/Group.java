package ru.etysoft.aurorauniverse.permissions;

import java.util.List;

public class Group {

    private List perms;
    private String name;

    public Group(String name, List<String> perms) {
        this.name = name;
        this.perms = perms;
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return perms;
    }

}
