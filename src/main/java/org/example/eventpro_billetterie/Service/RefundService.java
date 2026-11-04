package org.example.eventpro_billetterie.Service;

import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.RefundCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.RefundResponse;
import org.example.eventpro_billetterie.Entity.*;
import org.example.eventpro_billetterie.Entity.Enums.PaymentStatus;
import org.example.eventpro_billetterie.Entity.Enums.RefundStatus;
import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Exception.BusinessRuleException;
import org.example.eventpro_billetterie.Exception.RefundNotFound;
import org.example.eventpro_billetterie.Mapper.RefundMapper;
import org.example.eventpro_billetterie.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RefundMapper refundMapper;

    @Transactional
    public RefundResponse createRefund(RefundCreateRequest request, UUID clientId) {

        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new BusinessRuleException("Payment not found"));

        Reservation reservation = payment.getReservation();

        if (!reservation.getClient().getId().equals(clientId)) {
            throw new BusinessRuleException(
                    "You are not allowed to request a refund for this payment"
            );
        }

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BusinessRuleException(
                    "Only confirmed reservations can be refunded"
            );
        }

        // 5. Check payment status
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BusinessRuleException(
                    "Only successful payments can be refunded"
            );
        }

        Optional<Refund> refundAlreadyExist = refundRepository.findRefundByPayment(payment);
        if (refundAlreadyExist.isPresent()) {
            throw new BusinessRuleException(
                    "A refund request already exists for this payment"
            );
        }


        Refund refund = new Refund(
                payment,
                payment.getAmount(),
                request.getReason(),
                Instant.now(),
                RefundStatus.PENDING
        );

        Refund savedRefund = refundRepository.save(refund);

        return refundMapper.toResponse(savedRefund);
    }


    @Transactional
    public RefundResponse approveRefund(UUID refundId, UUID adminId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new BusinessRuleException("Refund not found"));

        isAdmin(adminId);

        if (refund.getStatus() != RefundStatus.PENDING) {
            throw new BusinessRuleException(
                    "Only pending refunds can be approved"
            );
        }

        refund.setStatus(RefundStatus.APPROVED);
        refund.setProcessedAt(Instant.now());

        Reservation reservation = refund.getPayment().getReservation();
        reservation.setStatus(ReservationStatus.REFUNDED);

        Refund savedRefund = refundRepository.save(refund);

        return refundMapper.toResponse(savedRefund);
    }

    @Transactional
    public RefundResponse rejectRefund(UUID refundId, UUID adminId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(RefundNotFound::new);

        isAdmin(adminId);

        if (refund.getStatus() != RefundStatus.PENDING) {
            throw new BusinessRuleException(
                    "Only pending refunds can be rejected"
            );
        }

        refund.setStatus(RefundStatus.REJECTED);
        refund.setProcessedAt(Instant.now());

        Refund savedRefund = refundRepository.save(refund);

        return refundMapper.toResponse(savedRefund);
    }

    private void isAdmin(UUID adminId) {

        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new BusinessRuleException("Admin not found"));

        if (!(user instanceof Admin)) {
            throw new BusinessRuleException(
                    "Only an admin can process refunds"
            );
        }

    }
}