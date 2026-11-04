package org.example.eventpro_billetterie.Exception;

public class TicketCategoryNotFound extends RuntimeException {
    public TicketCategoryNotFound(String message) {
        super(message);
    }

    public TicketCategoryNotFound() {
        super("Ticket Category Not Found");
    }
}
