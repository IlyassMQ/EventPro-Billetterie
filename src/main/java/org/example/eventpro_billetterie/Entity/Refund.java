package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.eventpro_billetterie.Entity.Enums.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Refund {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status;

    @Column(nullable = false)
    private Instant requestedAt;

    private Instant processedAt;

    public Refund(Payment payment, BigDecimal amount, String reason, Instant requestedAt, RefundStatus status) {
        this.payment = payment;
        this.amount = amount;
        this.reason = reason;
        this.requestedAt = requestedAt;
        this.status = status;
    }

}