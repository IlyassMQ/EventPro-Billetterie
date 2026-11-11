
package org.example.eventpro_billetterie.Service;

import org.example.eventpro_billetterie.DTO.Request.EventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.EventResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Entity.Enums.EventStatus;
import org.example.eventpro_billetterie.Mapper.EventMapper;
import org.example.eventpro_billetterie.Repository.EventRepository;
import org.example.eventpro_billetterie.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @Test
    void shouldCreateEvent() {


        UUID organizerId = UUID.randomUUID();

        EventCreateRequest request = EventCreateRequest.builder()
                .title("Java Conference")
                .description("A conference about Java")
                .location("Casablanca")
                .startDate(Instant.now().plusSeconds(86400))
                .endDate(Instant.now().plusSeconds(90000))
                .salesOpeningDate(Instant.now().plusSeconds(3600))
                .salesClosingDate(Instant.now().plusSeconds(80000))
                .capacity(500)
                .build();

        Organizer organizer = new Organizer("Ahmed", "ahmed@gmail.com","organisation");

        Event event = new Event();
        Event savedEvent = new Event();

        EventResponse expectedResponse = EventResponse.builder()
                .id(UUID.randomUUID())
                .title("Java Conference")
                .description("A conference about Java")
                .location("Casablanca")
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .salesOpeningDate(request.getSalesOpeningDate())
                .salesClosingDate(request.getSalesClosingDate())
                .capacity(500)
                .status("DRAFT")
                .build();

        when(userRepository.findById(organizerId))
                .thenReturn(Optional.of(organizer));

        when(eventMapper.toEntity(request))
                .thenReturn(event);

        when(eventRepository.save(event))
                .thenReturn(savedEvent);

        when(eventMapper.toResponse(savedEvent))
                .thenReturn(expectedResponse);

        EventResponse result = eventService.createEvent(request, organizerId);

        assertNotNull(result);
        assertEquals("Java Conference", result.getTitle());
        assertEquals("DRAFT", result.getStatus());

        assertEquals(organizer, event.getOrganizer());
        assertEquals(EventStatus.DRAFT, event.getStatus());

        verify(userRepository).findById(organizerId);
        verify(eventMapper).toEntity(request);
        verify(eventRepository).save(event);
        verify(eventMapper).toResponse(savedEvent);
    }
}
