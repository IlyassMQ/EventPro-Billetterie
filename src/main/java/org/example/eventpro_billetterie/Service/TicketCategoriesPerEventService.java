
package org.example.eventpro_billetterie.Service;

import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.TicketCategoryPerEventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.TicketCategoryPerEventResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.TicketCategory;
import org.example.eventpro_billetterie.Entity.TicketCategoriesPerEvent;
import org.example.eventpro_billetterie.Mapper.TicketCategoriesPerEventMapper;
import org.example.eventpro_billetterie.Repository.EventRepository;
import org.example.eventpro_billetterie.Repository.TicketCategoryRepository;
import org.example.eventpro_billetterie.Repository.TicketCategoriesPerEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketCategoriesPerEventService {

    private final TicketCategoriesPerEventRepository ticketCategoriesPerEventRepository;
    private final EventRepository eventRepository;
    private final TicketCategoryRepository ticketCategoryRepository;
    private final TicketCategoriesPerEventMapper ticketCategoriesPerEventMapper;

    @Transactional
    public TicketCategoryPerEventResponse create(TicketCategoryPerEventCreateRequest request) {

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Event not found"));


        TicketCategory category = ticketCategoryRepository
                .findById(request.getTicketCategoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Ticket category not found"));


        TicketCategoriesPerEvent ticketCategoriesPerEvent = ticketCategoriesPerEventMapper.toEntity(request);

        ticketCategoriesPerEvent.setEvent(event);
        ticketCategoriesPerEvent.setCategory(category);
        ticketCategoriesPerEvent.setAvailableQuantity(ticketCategoriesPerEvent.getTotalQuantity());


        TicketCategoriesPerEvent saved = ticketCategoriesPerEventRepository.save(ticketCategoriesPerEvent);

        return ticketCategoriesPerEventMapper.toResponse(saved);
    }

    public TicketCategoryPerEventResponse getById(UUID id) {

        TicketCategoriesPerEvent ticketCategoriesPerEvent = ticketCategoriesPerEventRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Event ticket category not found"));

        return ticketCategoriesPerEventMapper.toResponse(ticketCategoriesPerEvent);
    }
    
    public List<TicketCategoryPerEventResponse> getAllForEvent(UUID eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new IllegalArgumentException("Event not found");
        }

        return ticketCategoriesPerEventRepository.findByEvent_Id(eventId).stream()
                .map(ticketCategoriesPerEventMapper::toResponse)
                .toList();
    }
}
