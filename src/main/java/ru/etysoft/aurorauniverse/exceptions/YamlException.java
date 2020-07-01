package ru.etysoft.aurorauniverse.exceptions;

public class YamlException extends Throwable {

    Exception ex;

    public YamlException(Exception exeption) {
        ex = exeption;
    }

    public Exception getException() {
        return ex;
    }
}
