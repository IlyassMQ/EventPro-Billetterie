
package org.example.eventpro_billetterie.Service;

import org.example.eventpro_billetterie.DTO.Request.PaymentCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.PaymentResponse;
import org.example.eventpro_billetterie.Entity.Client;
import org.example.eventpro_billetterie.Entity.Payment;
import org.example.eventpro_billetterie.Entity.Reservation;
import org.example.eventpro_billetterie.Entity.Enums.PaymentStatus;
import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Mapper.PaymentMapper;
import org.example.eventpro_billetterie.Repository.PaymentRepository;
import org.example.eventpro_billetterie.Repository.ReservationRepository;
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
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldCreatePaymentSuccessfully() {

        UUID clientId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        PaymentCreateRequest request = mock(PaymentCreateRequest.class);

        when(request.getIdempotencyKey()).thenReturn("payment-key-123");
        when(request.getReservationId()).thenReturn(reservationId);

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(clientId);

        Reservation reservation = mock(Reservation.class);

        when(reservation.getClient()).thenReturn(client);
        when(reservation.getStatus()).thenReturn(ReservationStatus.PENDING_PAYMENT);
        when(reservation.getExpirationDate())
                .thenReturn(Instant.now().plusSeconds(3600));

        Payment payment = mock(Payment.class);

        PaymentResponse expectedResponse = mock(PaymentResponse.class);

        when(paymentRepository.findPaymentByIdempotencyKey("payment-key-123"))
                .thenReturn(Optional.empty());

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));

        when(paymentRepository.findPaymentByReservation(reservation))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment)).thenReturn(payment);

        when(paymentMapper.toResponse(payment)).thenReturn(expectedResponse);

        PaymentResponse result = paymentService.createPayment(request, clientId);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(payment).setStatus(PaymentStatus.SUCCESS);
        verify(payment).setPaidAt(any(Instant.class));
        verify(payment).setIdempotencyKey("payment-key-123");

        verify(reservation).setStatus(ReservationStatus.CONFIRMED);

        verify(ticketService).createTickets(reservation);
        verify(paymentRepository).save(payment);
        verify(paymentMapper).toResponse(payment);
    }
}
