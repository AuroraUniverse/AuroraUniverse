package ru.etysoft.aurorauniverse.utils;

import java.util.Date;

public class Timer {

    private double timestart;
    public Timer()
    {
        timestart = System.currentTimeMillis();
    }

    public String getStringSeconds()
    {
        return ((System.currentTimeMillis() - timestart) / 1000) + "";
    }

}
