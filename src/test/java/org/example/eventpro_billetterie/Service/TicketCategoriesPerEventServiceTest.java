
package org.example.eventpro_billetterie.Service;

import org.example.eventpro_billetterie.DTO.Request.TicketCategoryPerEventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.TicketCategoryPerEventResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.TicketCategory;
import org.example.eventpro_billetterie.Entity.TicketCategoriesPerEvent;
import org.example.eventpro_billetterie.Mapper.TicketCategoriesPerEventMapper;
import org.example.eventpro_billetterie.Repository.EventRepository;
import org.example.eventpro_billetterie.Repository.TicketCategoryRepository;
import org.example.eventpro_billetterie.Repository.TicketCategoriesPerEventRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketCategoriesPerEventServiceTest {

    @Mock
    private TicketCategoriesPerEventRepository ticketCategoriesPerEventRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketCategoryRepository ticketCategoryRepository;

    @Mock
    private TicketCategoriesPerEventMapper ticketCategoriesPerEventMapper;

    @InjectMocks
    private TicketCategoriesPerEventService ticketCategoriesPerEventService;


    @Test
    void shouldCreateTicketCategoryForEvent() {

        UUID eventId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        TicketCategoryPerEventCreateRequest request =
                mock(TicketCategoryPerEventCreateRequest.class);

        when(request.getEventId()).thenReturn(eventId);
        when(request.getTicketCategoryId()).thenReturn(categoryId);

        Event event = mock(Event.class);
        TicketCategory category = mock(TicketCategory.class);

        TicketCategoriesPerEvent entity =
                mock(TicketCategoriesPerEvent.class);

        TicketCategoryPerEventResponse response =
                mock(TicketCategoryPerEventResponse.class);

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(ticketCategoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(ticketCategoriesPerEventMapper.toEntity(request))
                .thenReturn(entity);

        when(entity.getTotalQuantity()).thenReturn(100);

        when(ticketCategoriesPerEventRepository.save(entity))
                .thenReturn(entity);

        when(ticketCategoriesPerEventMapper.toResponse(entity))
                .thenReturn(response);

        TicketCategoryPerEventResponse result =
                ticketCategoriesPerEventService.create(request);

        assertNotNull(result);
        assertSame(response, result);

        verify(entity).setEvent(event);
        verify(entity).setCategory(category);
        verify(entity).setAvailableQuantity(100);

        verify(ticketCategoriesPerEventRepository).save(entity);
    }


    @Test
    void shouldGetTicketCategoryById() {

        UUID id = UUID.randomUUID();

        TicketCategoriesPerEvent entity =
                mock(TicketCategoriesPerEvent.class);

        TicketCategoryPerEventResponse response =
                mock(TicketCategoryPerEventResponse.class);

        when(ticketCategoriesPerEventRepository.findById(id))
                .thenReturn(Optional.of(entity));

        when(ticketCategoriesPerEventMapper.toResponse(entity))
                .thenReturn(response);

        TicketCategoryPerEventResponse result =
                ticketCategoriesPerEventService.getById(id);

        assertNotNull(result);
        assertSame(response, result);
    }


    @Test
    void shouldThrowExceptionWhenTicketCategoryForEventDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(ticketCategoriesPerEventRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> ticketCategoriesPerEventService.getById(id)
        );
    }


    @Test
    void shouldGetAllCategoriesForEvent() {

        UUID eventId = UUID.randomUUID();

        TicketCategoriesPerEvent entity =
                mock(TicketCategoriesPerEvent.class);

        TicketCategoryPerEventResponse response =
                mock(TicketCategoryPerEventResponse.class);

        when(eventRepository.existsById(eventId))
                .thenReturn(true);

        when(ticketCategoriesPerEventRepository.findByEvent_Id(eventId))
                .thenReturn(List.of(entity));

        when(ticketCategoriesPerEventMapper.toResponse(entity))
                .thenReturn(response);

        List<TicketCategoryPerEventResponse> result =
                ticketCategoriesPerEventService.getAllForEvent(eventId);

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
    }


    @Test
    void shouldThrowExceptionWhenEventDoesNotExist() {

        UUID eventId = UUID.randomUUID();

        when(eventRepository.existsById(eventId))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> ticketCategoriesPerEventService.getAllForEvent(eventId)
        );

        verify(ticketCategoriesPerEventRepository, never())
                .findByEvent_Id(eventId);
    }
}
