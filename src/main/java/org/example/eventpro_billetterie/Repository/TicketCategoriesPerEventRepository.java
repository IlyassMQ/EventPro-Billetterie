package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.TicketCategoriesPerEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketCategoriesPerEventRepository extends JpaRepository<TicketCategoriesPerEvent,UUID> {
        List<TicketCategoriesPerEvent> findByEvent_Id(UUID eventId);
}
