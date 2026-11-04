package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;


@Getter
@Builder
@Jacksonized
public class ReservationCreateRequest {
    @NotEmpty(message = "At least one ticket category is required")
    @Valid
    private List<ReservationLineRequest> reservationLines;

    private String promoCode;

}
