package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class PaymentResponse {

    private UUID id;
    private UUID reservationId;
    private BigDecimal amount;
    private String status;
    private Instant paidAt;
}