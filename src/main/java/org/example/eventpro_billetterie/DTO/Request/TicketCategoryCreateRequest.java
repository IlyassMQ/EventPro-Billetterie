package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class TicketCategoryCreateRequest {

    @NotBlank(message = "Category name is required")
    private String name;
}