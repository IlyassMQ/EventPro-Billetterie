package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.ReservationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ReservationLineRepository extends JpaRepository<ReservationLine, UUID> {


    @Query("""
            select coalesce(sum(rl.quantity),0)  from ReservationLine rl
            where rl.reservation.status IN ('PENDING_PAYMENT','CONFIRMED')
            and rl.reservation.client.id = :clientId and rl.eventCategory.id = :eventCategoryId
""")

    Long countReservedQuantity(@Param("clientId") UUID clientId, @Param("eventCategoryId") UUID eventCategoryId);

}
