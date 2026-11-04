package org.example.eventpro_billetterie.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.TicketCategoryPerEventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.TicketCategoryPerEventResponse;
import org.example.eventpro_billetterie.Service.TicketCategoriesPerEventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-ticket-categories")
@RequiredArgsConstructor
public class TicketCategoriesPerEventController {

    private final TicketCategoriesPerEventService ticketCategoriesPerEventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketCategoryPerEventResponse create(@Valid @RequestBody TicketCategoryPerEventCreateRequest request) {
        return ticketCategoriesPerEventService.create(request);
    }

    @GetMapping("/{id}")
    public TicketCategoryPerEventResponse getById(@PathVariable UUID id) {
        return ticketCategoriesPerEventService.getById(id);
    }

    @GetMapping("/event/{eventId}")
    public List<TicketCategoryPerEventResponse> getAllForEvent(@PathVariable UUID eventId) {
        return ticketCategoriesPerEventService.getAllForEvent(eventId);
    }
}
