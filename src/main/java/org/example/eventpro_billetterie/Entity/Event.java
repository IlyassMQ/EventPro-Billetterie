package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.eventpro_billetterie.Entity.Enums.EventStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Event {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizer_id",nullable = false)
    private Organizer organizer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "event")
    private List<TicketCategoriesPerEvent> eventCategories = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    private List<Promotion> promotions = new ArrayList<>();

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Instant startDate;

    @Column(nullable = false)
    private Instant endDate;

    @Column(nullable = false)
    private Instant salesOpeningDate;

    @Column(nullable = false)
    private Instant salesClosingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Column(nullable = false)
    private int capacity;

    public Event(Organizer organizer, String title, String description, String location, Instant startDate, Instant salesOpeningDate, Instant endDate, Instant salesClosingDate, EventStatus status, int capacity) {
        this.organizer = organizer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.salesOpeningDate = salesOpeningDate;
        this.endDate = endDate;
        this.salesClosingDate = salesClosingDate;
        this.status = status;
        this.capacity = capacity;
    }
}
