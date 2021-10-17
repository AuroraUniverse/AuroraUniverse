package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.ArrayList;
import java.util.List;

public class TownTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        ArrayList<String> possibleArgs = new ArrayList<>();

        ArrayList<String> firstPossibleArg = new ArrayList<>();

        firstPossibleArg.add("new");
        firstPossibleArg.add("toggle");
        firstPossibleArg.add("set");
        firstPossibleArg.add("delete");
        firstPossibleArg.add("deposit");
        firstPossibleArg.add("invite");
        firstPossibleArg.add("kick");
        firstPossibleArg.add("leave");
        firstPossibleArg.add("rename");
        firstPossibleArg.add("spawn");
        firstPossibleArg.add("region");
        firstPossibleArg.add("claim");
        firstPossibleArg.add("unclaim");
        firstPossibleArg.add("withdraw");
        firstPossibleArg.add("accept");





        if (args.length >= 1) {
          if(args.length == 1)
          {
              possibleArgs.addAll(firstPossibleArg);
          }
          else if(args.length == 2)
          {
              if(args[0].equals("set"))
              {
                  possibleArgs.add("spawn");
                  possibleArgs.add("perm");
                  possibleArgs.add("group");
              }
              else if(args[0].equals("toggle"))
              {
                  possibleArgs.add("pvp");
                  possibleArgs.add("fire");
              }
              else if(args[0].equals("region"))
              {
                  possibleArgs.add("add");
                  possibleArgs.add("kick");
                  possibleArgs.add("give");
                  possibleArgs.add("reset");
              }

          }
          else if(args.length == 3)
          {
              if(args[0].equals("toggle"))
              {
                  possibleArgs.add("on");
                  possibleArgs.add("off");
              }
              else if(args[1].equals("perm"))
              {

                  for(String groupName : AuroraPermissions.getGroups().keySet())
                  {
                      possibleArgs.add(groupName);
                  }
              }
              if((args[0].equals("region") && (args[1].equals("give") | args[1].equals("add") | args[1].equals("region"))))
              {
                  Player player = (Player)  sender;
                  Resident resident = Residents.getResident(player.getName());
                  if(resident != null)
                  {
                      if(resident.hasTown())
                      {
                          Town town = resident.getTown();
                          for(Resident r : town.getResidents())
                          {
                              possibleArgs.add(r.getName());
                          }
                      }
                  }
              }
              if(args[1].equals("group"))
              {
                  Player player = (Player)  sender;
                  Resident resident = Residents.getResident(player.getName());
                  if(resident != null)
                  {
                      if(resident.hasTown())
                      {
                          for(String groupName : AuroraPermissions.getGroups().keySet())
                          {
                              possibleArgs.add(groupName);
                          }
                      }
                  }
              }


          }
          else if(args.length == 4)
          {
              if(args[1].equals("perm"))
              {
                  possibleArgs.add("build");
                  possibleArgs.add("destroy");
                  possibleArgs.add("use");
                  possibleArgs.add("switch");
              }
              if(args[1].equals("group"))
              {
                  Player player = (Player)  sender;
                  Resident resident = Residents.getResident(player.getName());
                  if(resident != null)
                  {
                      if(resident.hasTown())
                      {
                          Town town = resident.getTown();
                          for(Resident r : town.getResidents())
                          {
                              possibleArgs.add(r.getName());
                          }
                      }
                  }
              }
          }
        } else {

        }

        for(String arg : possibleArgs)
        {
            if(arg.contains(args[args.length - 1]))
            {
                result.add(arg);
            }
        }
        return result;
    }
}
