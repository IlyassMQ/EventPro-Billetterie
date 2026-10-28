package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, UUID> {
}
