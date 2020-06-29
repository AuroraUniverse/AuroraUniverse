package ru.etysoft.aurorauniverse.economy;

public class Bank {

    private String name;
    private double amount;
    private String owner;
    private boolean isPlayerAccount;

    public Bank(String accountname, double startamount, String player)
    {
        name = accountname;
        owner = player;
        amount = startamount;
        if(player.equals(name))
        {
            isPlayerAccount = true;
        }
    }

    public String getOwner()
    {
        return owner;
    }

    public boolean isPlayerAccount()
    {
        return isPlayerAccount;
    }

    public boolean hasAmount(double money)
    {
            return amount >= money;
    }
    public double getBalance()
    {
        return amount;
    }

    public void setBalance(double d)
    {
        amount = d;
    }

    public String getName()
    {
        return name;
    }

    public void deposit(double money)
    {
        amount += money;
    }

    public boolean withdraw(double money)
    {
        if(money > amount)
        {
            return false;
        }
        else
        {
            amount -= money;
            return true;
        }

    }

    public boolean withdraw(double money, Bank toBank)
    {
        if(money > amount)
        {
            return false;
        }
        else
        {
            amount -= money;
            toBank.deposit(money);
            return true;
        }

    }

}
