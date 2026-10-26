package org.example.eventpro_billetterie.DTO.Response;

import org.example.eventpro_billetterie.Entity.Enums.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;

public class PromotionResponse {

    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private Instant startDate;
    private Instant endDate;
    private Integer maxUsage;
}
