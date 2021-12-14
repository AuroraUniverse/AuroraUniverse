package ru.etysoft.aurorauniverse.exceptions;

public class AuctionPlaceException extends Exception {

    private String message;

    public AuctionPlaceException(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
