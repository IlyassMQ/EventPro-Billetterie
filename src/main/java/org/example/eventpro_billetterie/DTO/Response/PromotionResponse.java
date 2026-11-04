package org.example.eventpro_billetterie.DTO.Response;

import lombok.Builder;
import lombok.Getter;
import org.example.eventpro_billetterie.Entity.Enums.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PromotionResponse {

    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int maxUsage;
}
