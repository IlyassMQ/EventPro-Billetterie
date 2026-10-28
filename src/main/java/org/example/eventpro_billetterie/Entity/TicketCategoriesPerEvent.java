package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TicketCategoriesPerEvent {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id",nullable = false)
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticketcategory_id")
    private TicketCategory category;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int totalQuantity;

    @Column(nullable = false)
    private int availableQuantity;

    @Column(nullable = false)
    private int maxQuotaPerClient;

}
