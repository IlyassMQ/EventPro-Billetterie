package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class RefundResponse {

    private UUID id;
    private UUID paymentId;
    private BigDecimal amount;
    private String status;
    private Instant requestedAt;
    private Instant processedAt;
}