package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Organizer extends User{
    @Column(nullable = false)
    private String organisationName;

    @OneToMany(mappedBy = "organizer")
    private List<Event> events = new ArrayList<>();

    public Organizer(String userName, String email, String organisationName) {
        super(userName, email);
        this.organisationName = organisationName;
    }
}
