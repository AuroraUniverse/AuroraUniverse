package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.world.Resident;

public abstract class CommandPattern {

    private CommandSender sender;
    private Resident resident;
    private String[] args;

    public CommandPattern(CommandSender sender, Resident resident, String[] args)
    {
        this.sender = sender;
        this.resident = resident;
        this.args = args;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Resident getResident() {
        return resident;
    }

    public String[] getArgs() {
        return args;
    }
}
