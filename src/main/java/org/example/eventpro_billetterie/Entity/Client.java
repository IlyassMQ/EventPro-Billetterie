package org.example.eventpro_billetterie.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

    @Entity
@Getter
@Setter
@NoArgsConstructor


public class Client extends User{

    @Column(nullable = false)
    private String phone;

    public Client(String userName, String email, String phone) {
        super(userName, email);
        this.phone = phone;
    }
}
