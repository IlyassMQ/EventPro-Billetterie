package org.example.eventpro_billetterie.Exception;

public class AgentNotFound extends RuntimeException {
    public AgentNotFound(String message) {
        super(message);
    }

    public AgentNotFound() {
        super("Agent Not Found");
    }
}
