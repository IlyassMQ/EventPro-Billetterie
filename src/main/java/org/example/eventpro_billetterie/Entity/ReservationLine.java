package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReservationLine {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_category_id", nullable = false)
    private TicketCategoriesPerEvent eventCategory;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    public ReservationLine(Reservation reservation, TicketCategoriesPerEvent eventCategory, int quantity, BigDecimal unitPrice) {
        this.reservation = reservation;
        this.eventCategory = eventCategory;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}