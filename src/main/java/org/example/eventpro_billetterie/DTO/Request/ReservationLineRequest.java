package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ReservationLineRequest {

    @NotNull(message = "Ticket category per event ID is required")
    private UUID ticketCategoriesPerEventId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
}