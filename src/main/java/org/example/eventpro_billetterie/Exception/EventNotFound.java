package org.example.eventpro_billetterie.Exception;

public class EventNotFound extends RuntimeException {
    public EventNotFound(String message) {
        super(message);
    }

    public EventNotFound() {
        super("Event Not Found");
    }
}
