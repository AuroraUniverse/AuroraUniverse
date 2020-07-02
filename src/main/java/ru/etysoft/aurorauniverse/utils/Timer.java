package ru.etysoft.aurorauniverse.utils;

public class Timer {

    private double timestart;
    public Timer()
    {
        timestart = System.currentTimeMillis();
    }

    public String getStringSeconds()
    {
        return String.valueOf((System.currentTimeMillis() - timestart) / 1000);
    }

}
