
package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class EventCreateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private Instant startDate;

    @NotNull(message = "End date is required")
    private Instant endDate;

    @NotNull(message = "Sales opening date is required")
    private Instant salesOpeningDate;

    @NotNull(message = "Sales closing date is required")
    private Instant salesClosingDate;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be bigger than zero")
    private Integer capacity;
}
