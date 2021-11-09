package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.etysoft.aurorauniverse.chat.AuroraChat;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.ArrayList;
import java.util.List;

public class ChatTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        ArrayList<String> possibleArgs = new ArrayList<>();

        ArrayList<String> firstPossibleArg = new ArrayList<>();
       if(alias.equalsIgnoreCase("chat"))
       {
           firstPossibleArg.add("ignore");

           if (args.length == 1) {
               possibleArgs.addAll(firstPossibleArg);
           }
           else if (args.length >= 1) {

               if(args[0].equals("ignore"))
               {
                   possibleArgs.add(AuroraChat.getChannelName(AuroraChat.Channels.GLOBAL));
                   possibleArgs.add(AuroraChat.getChannelName(AuroraChat.Channels.LOCAL));
                   possibleArgs.add(AuroraChat.getChannelName(AuroraChat.Channels.TOWN));
                   possibleArgs.add(AuroraChat.getChannelName(AuroraChat.Channels.NATION));
               }
           }
           for (String arg : possibleArgs) {
               if (arg.contains(args[args.length - 1])) {
                   result.add(arg);
               }
           }
       }

        return result;
    }
}

