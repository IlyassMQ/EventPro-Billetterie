package org.example.eventpro_billetterie.Exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound() {
        super("User Not Found");
    }
}
