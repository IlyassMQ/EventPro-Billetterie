
package org.example.eventpro_billetterie.Service;

import org.example.eventpro_billetterie.DTO.Request.ReservationCreateRequest;
import org.example.eventpro_billetterie.DTO.Request.ReservationLineRequest;
import org.example.eventpro_billetterie.DTO.Response.ReservationResponse;
import org.example.eventpro_billetterie.Entity.*;
import org.example.eventpro_billetterie.Entity.Enums.EventStatus;
import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Mapper.ReservationMapper;
import org.example.eventpro_billetterie.Repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TicketCategoriesPerEventRepository ticketCategoriesPerEventRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private ReservationLineRepository reservationLineRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldCreateReservation() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Client client = new Client("Sara", "sara@gmail.com", "0612345678");

        Event event = new Event();
        event.setId(eventId);
        event.setStatus(EventStatus.PUBLISHED);

        TicketCategoriesPerEvent category = new TicketCategoriesPerEvent();
        category.setId(categoryId);
        category.setEvent(event);
        category.setUnitPrice(new BigDecimal("100"));
        category.setAvailableQuantity(50);
        category.setMaxQuotaPerClient(4);

        ReservationLineRequest lineRequest = ReservationLineRequest.builder()
                .ticketCategoriesPerEventId(categoryId)
                .quantity(2)
                .build();

        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .reservationLines(List.of(lineRequest))
                .promoCode(null)
                .build();

        Reservation reservation = new Reservation();

        ReservationResponse expectedResponse = ReservationResponse.builder()
                .id(UUID.randomUUID())
                .eventId(eventId)
                .totalAmount(new BigDecimal("200"))
                .status("PENDING_PAYMENT")
                .build();

        when(userRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(ticketCategoriesPerEventRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(reservationLineRepository.countReservedQuantity(client.getId(), categoryId))
                .thenReturn(0L);

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(reservationMapper.toEntity(request))
                .thenReturn(reservation);

        when(reservationMapper.toResponse(reservation))
                .thenReturn(expectedResponse);


        ReservationResponse result =
                reservationService.createReservation(request, clientId);


        assertNotNull(result);
        assertEquals("PENDING_PAYMENT", result.getStatus());
        assertEquals(new BigDecimal("200"), result.getTotalAmount());

        assertEquals(client, reservation.getClient());
        assertEquals(event, reservation.getEvent());
        assertEquals(ReservationStatus.PENDING_PAYMENT, reservation.getStatus());
        assertEquals(48, category.getAvailableQuantity());

        assertNotNull(reservation.getReservationDate());
        assertNotNull(reservation.getExpirationDate());

        assertTrue(reservation.getExpirationDate()
                .isAfter(reservation.getReservationDate()));

        verify(userRepository).findById(clientId);
        verify(ticketCategoriesPerEventRepository, times(2)).findById(categoryId);
        verify(reservationRepository).save(reservation);
        verify(reservationLineRepository).saveAll(anyList());
        verify(reservationMapper).toResponse(reservation);
    }
}
