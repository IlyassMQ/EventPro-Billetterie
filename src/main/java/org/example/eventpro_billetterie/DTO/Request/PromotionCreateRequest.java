package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.example.eventpro_billetterie.Entity.Enums.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Jacksonized
public class PromotionCreateRequest {

    @NotBlank(message = "Promotion code is required")
    private String code;

    @NotNull(message = "Event is required")
    private UUID eventId;

    @NotNull(message = "Discount type is required")
    private DiscountType discountType;

    @NotNull(message = "Discount value is required")
    @Positive(message = "Discount value must be greater than zero")
    private BigDecimal discountValue;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Maximum usage count is required")
    @Positive(message = "Maximum usage count must be greater than zero")
    private Integer maxUsage;
}