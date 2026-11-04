package org.example.eventpro_billetterie.DTO.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Getter
@Builder
@Jacksonized
public class PaymentCreateRequest {

    @NotNull
    private UUID reservationId;

    @NotBlank
    private String idempotencyKey;
}
