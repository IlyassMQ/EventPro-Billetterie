package org.example.eventpro_billetterie.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountCalculate {


    public static BigDecimal calculateFinalAmount(BigDecimal subtotal, BigDecimal discountPercentage) {

        BigDecimal discountAmount = subtotal.multiply(discountPercentage).divide(new BigDecimal(100),2, RoundingMode.HALF_UP);

        BigDecimal finalAmount = subtotal.subtract(discountAmount);

        return finalAmount;
    }
}
