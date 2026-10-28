package org.example.eventpro_billetterie.Service;


import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.ReservationCreateRequest;
import org.example.eventpro_billetterie.DTO.Request.ReservationLineRequest;
import org.example.eventpro_billetterie.DTO.Response.PromotionResponse;
import org.example.eventpro_billetterie.DTO.Response.ReservationResponse;
import org.example.eventpro_billetterie.Entity.*;
import org.example.eventpro_billetterie.Entity.Enums.DiscountType;
import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Exception.BusinessRuleException;
import org.example.eventpro_billetterie.Exception.PromotionNotFound;
import org.example.eventpro_billetterie.Exception.TicketCategoryNotFound;
import org.example.eventpro_billetterie.Exception.UserNotFound;
import org.example.eventpro_billetterie.Mapper.ReservationMapper;
import org.example.eventpro_billetterie.Repository.PromotionRepository;
import org.example.eventpro_billetterie.Repository.ReservationRepository;
import org.example.eventpro_billetterie.Repository.TicketCategoriesPerEventRepository;
import org.example.eventpro_billetterie.Repository.UserRepository;
import org.example.eventpro_billetterie.util.AmountCalculate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketCategoriesPerEventRepository ticketCategoriesPerEventRepository;
    private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;


    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request, UUID clientId) {
        User user = userRepository.findById(clientId)
                .orElseThrow(UserNotFound::new);

        if (!(user instanceof Client client)) {
            throw new BusinessRuleException("Only clients can create reservations");
        }

        if (request.getReservationLines() == null || request.getReservationLines().isEmpty()) {
            throw new BusinessRuleException("A reservation must contain at least one ticket");
        }
        UUID selectedEventId = null;
        for (ReservationLineRequest lineRequest : request.getReservationLines()) {

            TicketCategoriesPerEvent category = ticketCategoriesPerEventRepository
                    .findById(lineRequest.getTicketCategoriesPerEventId())
                    .orElseThrow(TicketCategoryNotFound::new);


            UUID currentEventId = category.getEvent().getId();

            if (selectedEventId == null) {
                selectedEventId = currentEventId;
            } else if (!selectedEventId.equals(currentEventId)) {
                throw new BusinessRuleException("All ticket categories must belong to the same event");
            }

            if (lineRequest.getQuantity() <= 0) {
                throw new BusinessRuleException("Ticket quantity must be greater than zero");
            }


            if (category.getMaxQuotaPerClient() < lineRequest.getQuantity()) {
                throw new BusinessRuleException(
                        "You cannot reserve more tickets than the maximum quota per client"
                );
            }

            if (category.getAvailableQuantity() < lineRequest.getQuantity()) {
                throw new BusinessRuleException(
                        "Not enough tickets available"
                );
            }

        }
            Promotion promotion = null;
        if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {
            promotion = promotionRepository.findByCode(request.getPromoCode()).
                    orElseThrow(PromotionNotFound::new);
            if (!promotion.getEvent().getId().equals(selectedEventId)) {
                throw new BusinessRuleException("The Promotion must belong to the event");
            }

            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(promotion.getStartDate()) || now.isAfter(promotion.getEndDate())) {
                throw new BusinessRuleException("Promotion is not currently active");
            }

            if (promotion.getUsageCount() >= promotion.getMaxUsage()) {
                throw new BusinessRuleException("Promotion usage limit has been reached");
            }

        }

        Reservation reservation = reservationMapper.toEntity(request);

        Instant reservationDate = Instant.now();
        reservation.setReservationDate(reservationDate);
        reservation.setExpirationDate(reservationDate.plus(10, ChronoUnit.MINUTES));
        reservation.setClient(client);
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);


        List<ReservationLine> reservationLineList = new ArrayList<>();

        BigDecimal totalAmmount = BigDecimal.ZERO;

        for (ReservationLineRequest lineRequest : request.getReservationLines()) {
            TicketCategoriesPerEvent category = ticketCategoriesPerEventRepository
                    .findById(lineRequest.getTicketCategoriesPerEventId())
                    .orElseThrow(TicketCategoryNotFound::new);


            int availableQuantity = category.getAvailableQuantity() - lineRequest.getQuantity();
            category.setAvailableQuantity(availableQuantity);

            ReservationLine reservationLine = new ReservationLine(reservation, category, lineRequest.getQuantity(), category.getUnitPrice());
            reservationLineList.add(reservationLine);


            BigDecimal totalWithoutPromo = category.getUnitPrice().multiply(BigDecimal.valueOf(lineRequest.getQuantity()));
            totalAmmount = totalAmmount.add(totalWithoutPromo);


        }


        if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {
            if (promotion.getDiscountType().equals(DiscountType.PERCENTAGE)) {
                totalAmmount = AmountCalculate.calculateFinalAmount(totalAmmount, promotion.getDiscountValue());
            } else if(totalAmmount.compareTo(promotion.getDiscountValue()) < 0 ){
                    throw new BusinessRuleException("The Discount is bigger then the totalAmount");
            }else {
                totalAmmount = totalAmmount.subtract(promotion.getDiscountValue());
            }


        }
        reservation.setReservationLines(reservationLineList);
        reservation.setTotalAmount(totalAmmount);
        reservationRepository.save(reservation);

        return reservationMapper.toResponse(reservation);
    }
    }
