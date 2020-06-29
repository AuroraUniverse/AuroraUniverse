package ru.etysoft.aurorauniverse.exceptions;

public class TownException extends Exception {

    private String error;
    public TownException(String message)
    {
        error = message;
    }

    public String  getMessageErr()
    {
        return  error;
    }
}
