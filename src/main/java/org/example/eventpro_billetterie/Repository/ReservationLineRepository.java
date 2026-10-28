package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.ReservationLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationLineRepository extends JpaRepository<ReservationLine, UUID> {

}
