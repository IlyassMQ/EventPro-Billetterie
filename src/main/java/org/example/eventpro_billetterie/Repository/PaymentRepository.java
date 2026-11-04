package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.Payment;
import org.example.eventpro_billetterie.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    Optional<Payment> findPaymentByReservation(Reservation reservation);

    Optional<Payment> findPaymentByIdempotencyKey(String idempotencyKey);
}
