package ru.etysoft.aurorauniverse.utils;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;

public class Permissions {

    public static boolean isAdmin(CommandSender sender, boolean sendMessage) {
        if (!sender.hasPermission("aun.admin")) {
            if (sendMessage) {
                sender.sendMessage(Messages.noPermission());
            }
            return false;
        }
        return true;
    }

    public static boolean canCreateNation(CommandSender p) {
        return p.hasPermission("nation.create");
    }

    public static boolean canInviteNation(CommandSender p) {
        return p.hasPermission("nation.invite");
    }
    public static boolean canTeleportOnTowns(CommandSender p) {
        return p.hasPermission("teleport.towns");
    }

    public static boolean canAcceptNationInvite(CommandSender p) {
        return p.hasPermission("nation.accept");
    }

    public static boolean canLeaveNation(CommandSender p) {
        return p.hasPermission("nation.leave");
    }

    public static boolean canKickNation(CommandSender p) {
        return p.hasPermission("nation.kick");
    }

    public static boolean canDeleteNation(CommandSender p) {
        return p.hasPermission("nation.delete");
    }

    public static boolean canEditTown(CommandSender p) {
        return p.hasPermission("aun.edittown");
    }

    public static boolean canCreateTown(CommandSender p) {
        return p.hasPermission("town.create");
    }


    public static boolean canRenameTown(CommandSender p) {
        return p.hasPermission("town.rename");
    }

    public static boolean canSetTax(CommandSender p) {
        return p.hasPermission("town.set.tax");
    }
    public static boolean canSetMayor(CommandSender p) {
        return p.hasPermission("town.set.mayor");
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

    public static boolean canSetGroup(CommandSender p) {
        return p.hasPermission("town.set.group");
    }

    public static boolean canTogglePvP(CommandSender p) {
        return p.hasPermission("town.toggle.pvp");
    }

    public static boolean canToggleMobs(CommandSender p) {
        return p.hasPermission("town.toggle.mobs");
    }

    public static boolean canToggleFire(CommandSender p) {
        return p.hasPermission("town.toggle.fire");
    }

    public static boolean canToggleExplosions(CommandSender p) {
        return p.hasPermission("town.toggle.explosions");
    }

    public static boolean canKickResident(CommandSender p) {
        return p.hasPermission("town.residents.kick");
    }

    public static boolean canInviteResident(CommandSender p) {
        return p.hasPermission("town.residents.invite");
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

    public static boolean canGetRegionInfo(CommandSender p) {
        return p.hasPermission("town.region.info");
    }

    public static boolean canKickMemberRegion(CommandSender p) {
        return p.hasPermission("town.region.kick");
    }
    public static boolean canTogglePvpRegion(CommandSender p) {
        return p.hasPermission("town.region.pvp");
    }

    public static boolean canBypassRegion(CommandSender p) {
        return p.hasPermission("town.region.bypass");
    }

    public static boolean canResetRegion(CommandSender p) {
        return p.hasPermission("town.region.reset");
    }
}
