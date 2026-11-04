package org.example.eventpro_billetterie.Exception;

public class PromotionAlreadyExistsException extends RuntimeException {
    public PromotionAlreadyExistsException(String message) {
        super(message);
    }

    public PromotionAlreadyExistsException() {
        super("Promotion Already Exists");
    }
}
