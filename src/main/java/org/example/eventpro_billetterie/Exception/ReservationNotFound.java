package org.example.eventpro_billetterie.Exception;

public class ReservationNotFound extends RuntimeException {
    public ReservationNotFound(String message) {
        super(message);
    }

    public ReservationNotFound() {
        super("Reservation Not Found");
    }
}


