package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ReservationLineResponse {

    private UUID id;
    private UUID ticketCategoriesPerEventId;
    private String categoryName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
