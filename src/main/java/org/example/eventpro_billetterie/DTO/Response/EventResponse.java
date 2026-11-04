package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;
import org.example.eventpro_billetterie.Entity.Promotion;
import org.example.eventpro_billetterie.Entity.TicketCategoriesPerEvent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class EventResponse {

    private UUID id;
    private String title;
    private String description;
    private String location;
    private List<TicketCategoryPerEventResponse> categories;
    private List<PromotionResponse> promotions;
    private Instant startDate;
    private Instant endDate;
    private Instant salesOpeningDate;
    private Instant salesClosingDate;
    private Integer capacity;
    private String status;
}