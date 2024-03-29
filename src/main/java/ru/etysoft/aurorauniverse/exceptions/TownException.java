package ru.etysoft.aurorauniverse.exceptions;

public class TownException extends Exception {

    private String error;
    public TownException(String message)
    {
        error = message;
    }

    @Override
    public String getMessage() {
        return error;
    }

    public String getErrorMessage()
    {
        return  error;
    }
}
