package ru.etysoft.aurorauniverse.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.world.Resident;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AuroraEconomy implements Economy {

    private static final Map<String, Bank> bankList = new HashMap<>();

    public boolean addBank(Bank bank)
    {
        if(!bankList.containsKey(bank.getName()))
        {
            bankList.put(bank.getName(), bank);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean canPay(Resident resident, double sum)
    {
        if(resident.getBalance() >= sum)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Bank getBank(String name)
    {
        return bankList.getOrDefault(name, null);
    }

    public boolean isBankExists(String name)
    {
        if(name == null) return false;
        return bankList.containsKey(name);
    }



    public void deleteBank(Bank bank)
    {
        bankList.remove(bank.getName());

    }
    public void deleteBankByName(String name)
    {
        bankList.remove(name);

    }

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
        return 0;
    }

    @Override
    public String format(double v) {
        return v + "";
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    @Deprecated
    public boolean hasAccount(String s) {
        return isBankExists(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return isBankExists(offlinePlayer.getName());
    }

    @Override
    @Deprecated
    public boolean hasAccount(String s, String s1) {
        return isBankExists(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {

        return isBankExists(offlinePlayer.getName());
    }

    @Override
    @Deprecated
    public double getBalance(String s) {
        return getBank(s).getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("[Vault] Getting " + offlinePlayer.getName() + "'s balance (" + getBank(offlinePlayer.getName()).getBalance() + ")");
        }
        return getBank(offlinePlayer.getName()).getBalance();
    }

    @Override
    @Deprecated
    public double getBalance(String s, String s1) {
        return getBank(s).getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("[Vault] Getting " + offlinePlayer.getName() + "'s balance (" + getBank(offlinePlayer.getName()).getBalance() + ")");
        }
        return getBank(offlinePlayer.getName()).getBalance();
    }

    @Override
    @Deprecated
    public boolean has(String s, double v) {
        Bank _bank = getBank(s);
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("[Vault] Checking if has amount " + v + " bank "+ s + " with balance " + getBank(s).getBalance() + ")");
        }
        return _bank.hasAmount(v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        Bank _bank = getBank(offlinePlayer.getName());
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("[Vault] Checking if has amount " + v + " bank "+ offlinePlayer.getName() + " with balance " + getBank(offlinePlayer.getName()).getBalance() + ")");
        }
        return _bank.hasAmount(v);
    }

    @Override
    @Deprecated
    public boolean has(String s, String s1, double v) {
        Bank _bank = getBank(s);
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("[Vault] Checking if has amount " + v + " bank "+ s + " with balance " + getBank(s).getBalance() + ")");
        }
        return _bank.hasAmount(v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        Bank _bank = getBank(offlinePlayer.getName());
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("[Vault] Checking if has amount " + v + " bank "+ offlinePlayer.getName() + " with balance " + getBank(offlinePlayer.getName()).getBalance() + ")");
        }
        return _bank.hasAmount(v);


    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String s, double v) {
        Bank _bank = getBank(s);
        if(_bank.withdraw(v))
        {
            return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.FAILURE, "not enough money");
        }

    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {

        return withdraw(offlinePlayer, v);
    }

    private EconomyResponse withdraw(OfflinePlayer offlinePlayer, double v) {
        Bank _bank = getBank(offlinePlayer.getName());
        if(_bank.withdraw(v))
        {
            if (AuroraLanguage.getDebugMode())
            {
                Logger.debug("Withdraw " + v + " from " + offlinePlayer.getName());
            }
            return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.FAILURE, "not enough money");
        }
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        Bank _bank = getBank(s);
        if(_bank.withdraw(v))
        {
            if (AuroraLanguage.getDebugMode())
            {
                Logger.debug("Withdraw " + v + " from " + s);
            }
            return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.FAILURE, "not enough money");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdraw(offlinePlayer, v);

    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String s, double v) {
        Resident r =  Residents.getResident(s);
        EconomyResponse response;
        if(r != null) {
            r.giveBalance(v);

            response = new EconomyResponse(v, r.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
            return response;
        }
        else
        {
            response = new EconomyResponse(v, r.getBalance(), EconomyResponse.ResponseType.FAILURE, "Can't find resident");
            return response;
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v)
    {
        String s = offlinePlayer.getName();

        if(isBankExists(s))
        {
        Bank _bank = getBank(s);
            if (AuroraLanguage.getDebugMode())
            {
                Logger.debug("Deposit " + v + " to " + s);
            }
            _bank.deposit(v);

        return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
        Logger.warning("Can't deposit: bank " + s + " doesn't exists!");
        return   new EconomyResponse(v, 0, EconomyResponse.ResponseType.SUCCESS, "Bank with name " + s + " doesn't exists!");
        }
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String s, String s1, double v) {

        if(isBankExists(s))
        {
            Bank _bank = getBank(s);
            if (AuroraLanguage.getDebugMode())
            {
                Logger.debug("Deposit " + v + " to " + s + " in world " + s1);
            }
            _bank.deposit(v);
            return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            Logger.warning("Can't deposit: bank " + s + " doesn't exists!");
          return   new EconomyResponse(v, 0, EconomyResponse.ResponseType.SUCCESS, "Bank with name " + s + " doesn't exists!");
        }

    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        if(isBankExists(offlinePlayer.getName()))
        {
            if (AuroraLanguage.getDebugMode())
            {
                Logger.debug("Deposit " + v + " to " + offlinePlayer.getName() + " in world " + s);
            }
            Bank _bank = getBank(offlinePlayer.getName());
            _bank.deposit(v);
            return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            Logger.warning("Can't deposit: bank " + offlinePlayer.getName() + " doesn't exists!");
            return   new EconomyResponse(v, 0, EconomyResponse.ResponseType.SUCCESS, "Bank with name " + s + " doesn't exists!");
        }
    }

    @Override
    @Deprecated
    public EconomyResponse createBank(String s, String player) {
        Bank b = new Bank(s, 0, player);
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("Try to create bank " + s + " to " + player);
        }
        if(addBank(b))
        {
            Logger.info("Created new bank account: " + s + " with owner " + player);
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Can't create bank with that name.");
        }

    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        Bank b = new Bank(s, 0, offlinePlayer.getName());
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("Try to create bank " + s + " to " + offlinePlayer.getName());
        }
        if(addBank(b))
        {
            Logger.info("Created new bank account: " + s + " with owner " + offlinePlayer.getName());
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Can't create bank with that name.");
        }
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        deleteBank(s);

            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankBalance(String s)
    {
        if(isBankExists(s))
        {
           Bank _bank = getBank(s);
           return new EconomyResponse(_bank.getBalance(), _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
       else
        {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "town doesn't exists");
        }

    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        Bank _bank = getBank(s);
        if(_bank.hasAmount(v))
        {
            return new EconomyResponse(_bank.getBalance(), _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        else
        {
            return new EconomyResponse(_bank.getBalance(), _bank.getBalance(), EconomyResponse.ResponseType.FAILURE, "Hasn't amount");
        }

    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {


           Bank _bank = getBank(s);
            if(_bank.withdraw(v))
            {
                return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
            }
           else
            {
                return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.FAILURE, "balance is very low for this amount");
            }

    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {

      if(isBankExists(s)){
          Bank _bank = getBank(s);
            _bank.deposit(v);

                return new EconomyResponse(v, _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);

        }
        else
        {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "town doesn't exists");
        }
    }

    @Override
    @Deprecated
    public EconomyResponse isBankOwner(String s, String nickname) {
        Bank _bank = getBank(s);
        if(nickname.equals(_bank.getOwner()))
        {
            return new EconomyResponse(_bank.getBalance(), _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(_bank.getBalance(), _bank.getBalance(), EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        Bank _bank = getBank(s);
        if(offlinePlayer.getName().equals(_bank.getOwner()))
        {
            return new EconomyResponse(_bank.getBalance(), _bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(_bank.getBalance(), _bank.getBalance(), EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    @Deprecated
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        List<String> list = new ArrayList<>();
        for(Bank bank : bankList.values()) {
            list.add(bank.getName());
        }


        return list;
    }

    @Override
    @Deprecated
    public boolean createPlayerAccount(String s) {
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("Try to create player bank " + s);
        }
        Bank b = new Bank(s, 0, s);
        return addBank(b);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("Try to create player bank " + offlinePlayer.getName());
        }
        Bank b = new Bank(offlinePlayer.getName(), 0, offlinePlayer.getName());
        return addBank(b);
    }

    @Override
    @Deprecated
    public boolean createPlayerAccount(String s, String world) {
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("Try to create player bank " + s + " in the world " + world);
        }
        Bank b = new Bank(s, 0, s);
        return addBank(b);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String world) {
        if (AuroraLanguage.getDebugMode())
        {
            Logger.debug("Try to create player bank " + offlinePlayer.getName() + " in the world " + world);
        }
        Bank b = new Bank(offlinePlayer.getName(), 0, offlinePlayer.getName());
        return addBank(b);
    }
}