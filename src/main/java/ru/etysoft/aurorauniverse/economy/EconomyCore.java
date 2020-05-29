package ru.etysoft.aurorauniverse.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import ru.etysoft.aurorauniverse.Resident;
import ru.etysoft.aurorauniverse.Town;
import ru.etysoft.aurorauniverse.TownFun;

import java.util.List;

public class EconomyCore implements Economy {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "AuroraEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 42;
    }

    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String s) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String s) {
        return TownFun.getResident(s).getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return 42;
    }

    @Override
    public double getBalance(String s, String s1) {
        return 42;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return 42;
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        Resident r =  TownFun.getResident(s);
        EconomyResponse response;
        if( r.takeBalance(v))
        {
           response = new EconomyResponse(v, r.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            response = new EconomyResponse(v, r.getBalance(), EconomyResponse.ResponseType.FAILURE, "balance is very low for this amount");
        }
       return response;

    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        Resident r =  TownFun.getResident(s);
        EconomyResponse response;
         r.giveBalance(v);

            response = new EconomyResponse(v, r.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);

        return response;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s)
    {
        if(TownFun.isTownExists(s))
        {
            Town t = TownFun.getTown(s);
           return new EconomyResponse(t.getBank(), t.getBank(), EconomyResponse.ResponseType.SUCCESS, null);
        }
       else
        {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "town doesn't exists");
        }

    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {

        if(TownFun.isTownExists(s))
        {
            Town t = TownFun.getTown(s);
            if(t.withdrawBank(v))
            {
                return new EconomyResponse(v, t.getBank(), EconomyResponse.ResponseType.SUCCESS, null);
            }
           else
            {
                return new EconomyResponse(v, t.getBank(), EconomyResponse.ResponseType.FAILURE, "balance is very low for this amount");
            }
        }
        else
        {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "town doesn't exists");
        }
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {

        if(TownFun.isTownExists(s))
        {
            Town t = TownFun.getTown(s);
            t.depositBank(v);

                return new EconomyResponse(v, t.getBank(), EconomyResponse.ResponseType.SUCCESS, null);

        }
        else
        {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "town doesn't exists");
        }
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}