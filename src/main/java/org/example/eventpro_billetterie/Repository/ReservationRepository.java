package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation,UUID> {

    List<Reservation> findByStatusAndExpirationDateLessThanEqual(ReservationStatus status, Instant now);



    @Query("""
      select Reservation, count(ReservationLine.quantity) from Client
      join Reservation on Reservation.client.id = Client.id
      join ReservationLine on Reservation.id = ReservationLine.reservation.id
      join TicketCategoriesPerEvent on TicketCategoriesPerEvent.id = ReservationLine.eventCategory.id
      join Event on Event.id = TicketCategoriesPerEvent.event.id
            
      where Reservation.status = ReservationStatus.CONFIRMED
      and Client.email =:emailClient
     and ReservationLine.eventCategory.category.name =:categoryName
     and Event.id =:eventId
     having count(ReservationLine.quantity) >=:qteMin
         
""")
    Optional<Reservation> getRservationConfirmer(@Param("emailOr") String email,@Param("categoryName") String categoryName,@Param("qteMin") int qtMinimal,@Param("eventId") UUID eventId);
}