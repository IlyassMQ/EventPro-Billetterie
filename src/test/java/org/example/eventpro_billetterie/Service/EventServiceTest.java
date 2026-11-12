
package org.example.eventpro_billetterie.Service;

import org.example.eventpro_billetterie.DTO.Request.EventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.EventResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Entity.Enums.EventStatus;
import org.example.eventpro_billetterie.Entity.User;
import org.example.eventpro_billetterie.Exception.BusinessRuleException;
import org.example.eventpro_billetterie.Exception.EventNotFound;
import org.example.eventpro_billetterie.Exception.UserNotFound;
import org.example.eventpro_billetterie.Mapper.EventMapper;
import org.example.eventpro_billetterie.Repository.EventRepository;
import org.example.eventpro_billetterie.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
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




    @Test
    void shouldGetEventById() {
        UUID eventId = UUID.randomUUID();

        Event event = new Event();

        EventResponse expectedResponse = EventResponse.builder()
                .id(eventId)
                .title("Java Conference")
                .status("DRAFT")
                .build();

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(eventMapper.toResponse(event))
                .thenReturn(expectedResponse);

        EventResponse result = eventService.getEventById(eventId);

        assertNotNull(result);
        assertEquals("Java Conference", result.getTitle());

        verify(eventRepository).findById(eventId);
        verify(eventMapper).toResponse(event);
    }

    @Test
    void shouldThrowExceptionWhenEventDoesNotExist() {
        UUID eventId = UUID.randomUUID();

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.empty());

        assertThrows(EventNotFound.class,
                () -> eventService.getEventById(eventId));

        verify(eventRepository).findById(eventId);
        verifyNoInteractions(eventMapper);
    }

    @Test
    void shouldGetAllEvents() {
        Event event1 = new Event();
        Event event2 = new Event();

        EventResponse response1 = EventResponse.builder()
                .title("Java Conference")
                .build();

        EventResponse response2 = EventResponse.builder()
                .title("Spring Boot Workshop")
                .build();

        when(eventRepository.findAll())
                .thenReturn(List.of(event1, event2));

        when(eventMapper.toResponse(event1)).thenReturn(response1);
        when(eventMapper.toResponse(event2)).thenReturn(response2);

        List<EventResponse> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        assertEquals("Java Conference", result.get(0).getTitle());
        assertEquals("Spring Boot Workshop", result.get(1).getTitle());

        verify(eventRepository).findAll();
        verify(eventMapper, times(2)).toResponse(any(Event.class));
    }

    @Test
    void shouldThrowExceptionWhenOrganizerDoesNotExist() {
        UUID organizerId = UUID.randomUUID();

        when(userRepository.findById(organizerId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFound.class,
                () -> eventService.getOrganizer(organizerId));

        verify(userRepository).findById(organizerId);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOrganizer() {
        UUID userId = UUID.randomUUID();

        User user = new User();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.getOrganizer(userId)
        );

        assertEquals("The user is not an organizer", exception.getMessage());

        verify(userRepository).findById(userId);
    }

    @Test
    void shouldPublishDraftEvent() {
        UUID eventId = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();

        Organizer organizer = mock(Organizer.class);
        when(organizer.getId()).thenReturn(organizerId);

        Organizer eventOrganizer = mock(Organizer.class);
        when(eventOrganizer.getId()).thenReturn(organizerId);

        Event event = mock(Event.class);

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(userRepository.findById(organizerId))
                .thenReturn(Optional.of(organizer));

        when(event.getOrganizer()).thenReturn(eventOrganizer);
        when(event.getStatus()).thenReturn(EventStatus.DRAFT);

        EventResponse expectedResponse = EventResponse.builder()
                .title("Java Conference")
                .status("PUBLISHED")
                .build();

        when(eventMapper.toResponse(event)).thenReturn(expectedResponse);

        EventResponse result = eventService.publishEvent(eventId, organizerId);

        assertNotNull(result);
        assertEquals("PUBLISHED", result.getStatus());

        verify(event).setStatus(EventStatus.PUBLISHED);
        verify(eventRepository).save(event);
        verify(eventMapper).toResponse(event);
    }

    @Test
    void shouldThrowExceptionWhenEventIsAlreadyPublished() {
        UUID eventId = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();

        Organizer organizer = mock(Organizer.class);
        when(organizer.getId()).thenReturn(organizerId);

        Event event = mock(Event.class);

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(userRepository.findById(organizerId))
                .thenReturn(Optional.of(organizer));

        when(event.getOrganizer()).thenReturn(organizer);
        when(event.getStatus()).thenReturn(EventStatus.PUBLISHED);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> eventService.publishEvent(eventId, organizerId)
        );

        assertEquals("The Event is already Published", exception.getMessage());

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void shouldThrowExceptionWhenOnlyOrganizerCanPublishEvent() {
        UUID eventId = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();
        UUID anotherOrganizerId = UUID.randomUUID();

        Organizer organizer = mock(Organizer.class);
        when(organizer.getId()).thenReturn(organizerId);

        Organizer eventOrganizer = mock(Organizer.class);
        when(eventOrganizer.getId()).thenReturn(anotherOrganizerId);

        Event event = mock(Event.class);

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(userRepository.findById(organizerId))
                .thenReturn(Optional.of(organizer));

        when(event.getOrganizer()).thenReturn(eventOrganizer);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> eventService.publishEvent(eventId, organizerId)
        );

        assertEquals("Only Organizer can Publish the event ", exception.getMessage());

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void shouldThrowExceptionWhenEventIsNotDraft() {
        UUID eventId = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();

        Organizer organizer = mock(Organizer.class);
        when(organizer.getId()).thenReturn(organizerId);

        Event event = mock(Event.class);

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(userRepository.findById(organizerId))
                .thenReturn(Optional.of(organizer));

        when(event.getOrganizer()).thenReturn(organizer);
        when(event.getStatus()).thenReturn(EventStatus.COMPLETED);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> eventService.publishEvent(eventId, organizerId)
        );

        assertEquals("Only draft event can be published", exception.getMessage());

        verify(eventRepository, never()).save(any(Event.class));
    }

}
