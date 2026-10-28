package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    List<Promotion> findByEventId(UUID eventId);

    Optional<Promotion> findByCode(String code);

    boolean existsByEventIdAndCodeIgnoreCase(UUID eventId, String code);
}