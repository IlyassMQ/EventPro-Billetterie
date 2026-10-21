package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationLine> reservationLines = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private Instant reservationDate;

    @Column(nullable = false)
    private Instant expirationDate;

    public Reservation(Client client, Event event, ReservationStatus status, BigDecimal totalAmount, Instant reservationDate, Instant expirationDate) {
        this.client = client;
        this.event = event;
        this.status = status;
        this.totalAmount = totalAmount;
        this.reservationDate = reservationDate;
        this.expirationDate = expirationDate;
    }
}