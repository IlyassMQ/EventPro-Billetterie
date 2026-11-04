package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation,UUID> {

    List<Reservation> findByStatusAndExpirationDateLessThanEqual(ReservationStatus status, Instant now);
}
