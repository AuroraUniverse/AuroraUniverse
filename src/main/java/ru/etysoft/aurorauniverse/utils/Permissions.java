package ru.etysoft.aurorauniverse.utils;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;

public class Permissions {

    public static boolean isAdmin(CommandSender sender, boolean sendMessage)
    {
        if(!sender.hasPermission("aun.admin"))
        {
            if(sendMessage)
            {
                sender.sendMessage(Messages.noPermission());
            }
           return false;
        }
        return true;
    }

    public static boolean canCreateTown(CommandSender p) {
        return p.hasPermission("town.create");
    }

    public static boolean canDeleteTown(CommandSender p) {
        return p.hasPermission("town.delete");
    }

    public static boolean canClaim(CommandSender p) {
        return p.hasPermission("town.claim");
    }

    public static boolean canUnClaim(CommandSender p) {
        return p.hasPermission("town.unclaim");
    }

    public static boolean canLeaveTown(CommandSender p) {
        return p.hasPermission("town.leave");
    }

    public static boolean canDepositTown(CommandSender p) {
        return p.hasPermission("town.deposit");
    }

    public static boolean canWithdrawTown(CommandSender p) {
        return p.hasPermission("town.withdraw");
    }

    public static boolean canSetSpawn(CommandSender p) {
        return p.hasPermission("town.set.spawn");
    }

    public static boolean canSetPermissions(CommandSender p) {
        return p.hasPermission("town.set.perms");
    }

    public static boolean canTogglePvP(CommandSender p) {
        return p.hasPermission("town.toggle.pvp");
    }

    public static boolean canToggleFire(CommandSender p) {
        return p.hasPermission("town.toggle.fire");
    }

    public static boolean canKickResident(CommandSender p) {
        return p.hasPermission("town.kick");
    }

    public static boolean canInviteResident(CommandSender p) {
        return p.hasPermission("town.invite");
    }

    public static boolean canTeleportSpawn(CommandSender p) {
        return p.hasPermission("town.teleport.spawn");
    }

    public static boolean canPay(CommandSender p) {
        return p.hasPermission("auneconomy.pay");
    }

    public static boolean canTake(CommandSender p) {
        return p.hasPermission("auneconomy.take");
    }

    public static boolean canGive(CommandSender p) {
        return p.hasPermission("auneconomy.give");
    }

    public static boolean canBalance(CommandSender p) {
        return p.hasPermission("auneconomy.balance");
    }

    public static boolean canGiveRegion(CommandSender p) {
        return p.hasPermission("town.region.give");
    }

    public static boolean canAddMemberRegion(CommandSender p) {
        return p.hasPermission("town.region.addmember");
    }

    public static boolean canKickMemberRegion(CommandSender p) {
        return p.hasPermission("town.region.kick");
    }
    public static boolean canBypassRegion(CommandSender p) {
        return p.hasPermission("town.region.bypass");
    }

    public static boolean canResetRegion(CommandSender p) {
        return p.hasPermission("town.region.reset");
    }
}
