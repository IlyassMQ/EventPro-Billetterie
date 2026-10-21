package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ControlAgent extends User {
    public ControlAgent(String userName, String email) {
        super(userName, email);
    }

}
