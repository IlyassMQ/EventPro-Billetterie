package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation,UUID> {
    
}
