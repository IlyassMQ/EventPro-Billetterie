package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    @Query("""
    SELECT t FROM Ticket t
    WHERE t.reservationLine.reservation.client.id = :clientId
    """)
    List<Ticket> findTicketsByClientId(@Param("clientId") UUID clientId);
}
