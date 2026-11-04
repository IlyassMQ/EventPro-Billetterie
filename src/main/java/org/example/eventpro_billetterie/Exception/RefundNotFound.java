package org.example.eventpro_billetterie.Exception;

public class RefundNotFound extends RuntimeException {
    public RefundNotFound(String message) {
        super(message);
    }

    public RefundNotFound() {
        super("Refund Not Found");
    }
}
