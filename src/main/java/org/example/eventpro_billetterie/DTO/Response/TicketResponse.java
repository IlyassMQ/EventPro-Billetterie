package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class TicketResponse {

    private UUID id;
    private UUID reservationLineId;
    private String ticketCode;
    private String status;
    private Instant issuedAt;
}