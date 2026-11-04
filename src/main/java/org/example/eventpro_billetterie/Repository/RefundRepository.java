package org.example.eventpro_billetterie.Repository;

import org.example.eventpro_billetterie.Entity.Payment;
import org.example.eventpro_billetterie.Entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {
    
    Optional<Refund> findRefundByPayment(Payment payment);
}
