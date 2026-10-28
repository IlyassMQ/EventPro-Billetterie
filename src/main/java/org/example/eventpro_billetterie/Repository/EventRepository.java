package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Mapper.EventMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
