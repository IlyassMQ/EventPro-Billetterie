package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ReservationDetailsResponse {

    private UUID id;
    private UUID eventId;
    private String eventTitle;
    private BigDecimal totalAmount;
    private String status;
    private Instant createdAt;
    private Instant expirationDate;

    private List<ReservationLineResponse> reservationLines;
}
