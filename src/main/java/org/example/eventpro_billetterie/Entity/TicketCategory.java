package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class TicketCategory {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<TicketCategoriesPerEvent> eventCategories = new ArrayList<>();

    public TicketCategory(String name) {
        this.name = name;
    }
}
