package org.example.eventpro_billetterie.Service;


import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.PaymentCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.PaymentResponse;
import org.example.eventpro_billetterie.Entity.Enums.PaymentStatus;
import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Entity.Payment;
import org.example.eventpro_billetterie.Entity.Reservation;
import org.example.eventpro_billetterie.Entity.Ticket;
import org.example.eventpro_billetterie.Exception.BusinessRuleException;
import org.example.eventpro_billetterie.Exception.ReservationNotFound;
import org.example.eventpro_billetterie.Mapper.PaymentMapper;
import org.example.eventpro_billetterie.Repository.PaymentRepository;
import org.example.eventpro_billetterie.Repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentMapper paymentMapper;
    private final TicketService ticketService;

    @Transactional
    public PaymentResponse createPayment(PaymentCreateRequest request, UUID clientId) {


        Optional<Payment> existingPayment = paymentRepository.findPaymentByIdempotencyKey(request.getIdempotencyKey());

        if (existingPayment.isPresent()) {

            Payment payment = existingPayment.get();

            if (!payment.getReservation().getClient().getId().equals(clientId)){
                throw new BusinessRuleException(
                        "You are not allowed to pay for this reservation"
                );
            }

            if (!payment.getReservation().getId().equals(request.getReservationId())) {
                throw new BusinessRuleException(
                        "This idempotency key belongs to another reservation"
                );
            }

            return paymentMapper.toResponse(payment);
        }

        Reservation reservation = reservationRepository.findById(request.getReservationId()).
                orElseThrow(ReservationNotFound::new);

        if (!reservation.getClient().getId().equals(clientId)) {
            throw new BusinessRuleException(
                    "You are not allowed to pay for this reservation"
            );
        }

        if (reservation.getStatus() != ReservationStatus.PENDING_PAYMENT) {
            throw new BusinessRuleException(
                    "Only pending reservations can be paid"
            );
        }

        if (!Instant.now().isBefore(reservation.getExpirationDate())) {
            throw new BusinessRuleException(
                    "This reservation has expired"
            );
        }

        Payment payment = paymentRepository.findPaymentByReservation(reservation)
                .orElseGet(() -> new Payment(
                        reservation,
                        reservation.getTotalAmount(),
                        PaymentStatus.PENDING,
                        request.getIdempotencyKey(),
                        Instant.now()
                ));

        boolean paymentSuccessful = true;

        if (paymentSuccessful) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(Instant.now());
            reservation.setStatus(ReservationStatus.CONFIRMED);
            ticketService.createTickets(reservation);

        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        payment.setIdempotencyKey(request.getIdempotencyKey());
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }
}
