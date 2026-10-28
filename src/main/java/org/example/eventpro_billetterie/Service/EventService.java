package org.example.eventpro_billetterie.Service;

import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.EventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.EventResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.Enums.EventStatus;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Entity.User;
import org.example.eventpro_billetterie.Exception.EventNotFound;
import org.example.eventpro_billetterie.Exception.UserNotFound;
import org.example.eventpro_billetterie.Mapper.EventMapper;
import org.example.eventpro_billetterie.Repository.EventRepository;
import org.example.eventpro_billetterie.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponse createEvent(EventCreateRequest createRequest, UUID organizerId) {

        User user = userRepository.findById(organizerId)
                .orElseThrow(UserNotFound::new);

        if (!(user instanceof Organizer organizer)) {
            throw new IllegalArgumentException("The user is not an organizer");
        }

        Event event = eventMapper.toEntity( createRequest);

        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        Event savedEvent = eventRepository.save(event);

        return eventMapper.toResponse(savedEvent);
    }


    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(EventNotFound::new);

        return eventMapper.toResponse(event);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toResponse).toList();
    }
}