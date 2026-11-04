package org.example.eventpro_billetterie.Exception;

public class PromotionNotFound extends RuntimeException {
    public PromotionNotFound(String message) {
        super(message);
    }

    public PromotionNotFound(){
        super("Promotion Not Found");
    }
}
