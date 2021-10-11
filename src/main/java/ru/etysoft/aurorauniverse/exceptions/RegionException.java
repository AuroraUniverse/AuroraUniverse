package ru.etysoft.aurorauniverse.exceptions;

public class RegionException extends Exception {

    private String error;
    public RegionException(String message)
    {
        error = message;
    }

    public String getErrorMessage()
    {
        return  error;
    }
}
