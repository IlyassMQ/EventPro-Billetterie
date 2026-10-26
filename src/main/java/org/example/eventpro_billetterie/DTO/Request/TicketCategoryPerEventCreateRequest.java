package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class TicketCategoryPerEventCreateRequest {

    @NotNull(message = "Event ID is required")
    private UUID eventId;

    @NotNull(message = "Ticket category ID is required")
    private UUID ticketCategoryId;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be greater than zero")
    private BigDecimal unitPrice;

    @Positive(message = "Total quantity must be greater than zero")
    private int totalQuantity;

    @Positive(message = "Maximum quota per client must be greater than zero")
    private int maxQuotaPerClient;
}