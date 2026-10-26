package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TicketCategoryCreateRequest {

    @NotBlank(message = "Category name is required")
    private String name;
}