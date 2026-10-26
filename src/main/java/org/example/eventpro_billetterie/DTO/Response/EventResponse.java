package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class EventResponse {

    private UUID id;
    private String title;
    private String description;
    private String location;
    private Instant startDate;
    private Instant endDate;
    private Instant salesOpeningDate;
    private Instant salesClosingDate;
    private Integer capacity;
    private String status;
}