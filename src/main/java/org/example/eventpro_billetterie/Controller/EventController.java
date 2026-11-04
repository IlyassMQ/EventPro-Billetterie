package org.example.eventpro_billetterie.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.EventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.EventResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/create/{organizerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@Valid @RequestBody EventCreateRequest request, @PathVariable UUID organizerId) {
        return eventService.createEvent(request,organizerId);
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable UUID id) {
        return eventService.getEventById(id);
    }

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PatchMapping("/{organizerId}/{eventId}")
    public EventResponse publishEvent(@PathVariable UUID eventId,@PathVariable UUID organizerId){
        return eventService.publishEvent(eventId,organizerId);
    }
}