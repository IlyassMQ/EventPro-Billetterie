package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class EventCategoryResponse {

    private UUID id;
    private UUID eventId;
    private UUID ticketCategoryId;
    private String categoryName;
    private BigDecimal unitPrice;
    private int totalQuantity;
    private int maxQuotaPerClient;
}