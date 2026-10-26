package org.example.eventpro_billetterie.DTO.Response;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TicketCategoryResponse {

    private UUID id;
    private String name;
}
