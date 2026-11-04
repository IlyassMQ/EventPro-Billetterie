package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Getter
@Builder
@Jacksonized
public class RefundCreateRequest {

    @NotNull
    private UUID paymentId;

    @NotBlank
    private String reason;
}