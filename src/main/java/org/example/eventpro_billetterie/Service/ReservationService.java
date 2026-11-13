package org.example.eventpro_billetterie.Service;

import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.ReservationCreateRequest;
import org.example.eventpro_billetterie.DTO.Request.ReservationLineRequest;
import org.example.eventpro_billetterie.DTO.Response.ReservationResponse;
import org.example.eventpro_billetterie.Entity.*;
import org.example.eventpro_billetterie.Entity.Enums.DiscountType;
import org.example.eventpro_billetterie.Entity.Enums.EventStatus;
import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Exception.*;
import org.example.eventpro_billetterie.Mapper.EventMapper;
import org.example.eventpro_billetterie.Mapper.ReservationMapper;
import org.example.eventpro_billetterie.Repository.*;
import org.example.eventpro_billetterie.util.AmountCalculate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketCategoriesPerEventRepository ticketCategoriesPerEventRepository;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationLineRepository reservationLineRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request, UUID clientId) {

        Client client = getClient(clientId);

        validateReservationRequest(request);

        UUID eventId = validateTicketCategories(request.getReservationLines(), client);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFound::new);

        Promotion promotion = validatePromotion(request.getPromoCode(), eventId);

        Reservation reservation = createReservationEntity(request, client);
        reservation.setEvent(event);

        List<ReservationLine> lines = createReservationLines(reservation, request.getReservationLines());

        BigDecimal totalAmount = calculateTotalAmount(lines, promotion);

        reservation.setReservationLines(lines);
        reservation.setTotalAmount(totalAmount);

        reservationRepository.save(reservation);
        reservationLineRepository.saveAll(lines);

        return reservationMapper.toResponse(reservation);
    }

    public Client getClient(UUID clientId) {

        User user = userRepository.findById(clientId)
                .orElseThrow(UserNotFound::new);

        if (!(user instanceof Client client)) {
            throw new BusinessRuleException("Only clients can create reservations");
        }

        return client;
    }

    public void validateReservationRequest(ReservationCreateRequest request) {

        if (request.getReservationLines() == null || request.getReservationLines().isEmpty()) {
            throw new BusinessRuleException("A reservation must contain at least one ticket");
        }
    }


    public TicketCategoriesPerEvent findTicketCategory(UUID categoryId) {
        return ticketCategoriesPerEventRepository.findById(categoryId)
                .orElseThrow(TicketCategoryNotFound::new);
    }

    public UUID validateTicketCategories(List<ReservationLineRequest> lines, Client client) {

        UUID selectedEventId = null;

        for (ReservationLineRequest lineRequest : lines) {

            TicketCategoriesPerEvent category = findTicketCategory(lineRequest.getTicketCategoriesPerEventId());

            UUID currentEventId = category.getEvent().getId();

            if (selectedEventId == null) {
                selectedEventId = currentEventId;
            } else if (!selectedEventId.equals(currentEventId)) {
                throw new BusinessRuleException("All ticket categories must belong to the same event");
            }

            if (lineRequest.getQuantity() <= 0) {
                throw new BusinessRuleException("Ticket quantity must be greater than zero");
            }

            long countReserved = reservationLineRepository.countReservedQuantity(client.getId(), category.getId()) + lineRequest.getQuantity();

            if (countReserved > category.getMaxQuotaPerClient()) {
                throw new BusinessRuleException("You cannot reserve more tickets than the maximum quota per client");
            }

            if (category.getAvailableQuantity() < lineRequest.getQuantity()) {
                throw new BusinessRuleException("Not enough tickets available");
            }

            if (category.getEvent().getStatus() != EventStatus.PUBLISHED) {
                throw new BusinessRuleException(
                        "Reservations are only allowed for published events");
            }
        }

        return selectedEventId;
    }


    public Promotion validatePromotion(String promoCode, UUID eventId) {

        if (promoCode == null || promoCode.isBlank()) {
            return null;
        }

        Promotion promotion = promotionRepository.findByCode(promoCode)
                .orElseThrow(PromotionNotFound::new);

        if (!promotion.getEvent().getId().equals(eventId)) {
            throw new BusinessRuleException(
                    "The promotion must belong to the event");
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(promotion.getStartDate()) || now.isAfter(promotion.getEndDate())) {
            throw new BusinessRuleException(
                    "Promotion is not currently active");
        }

        if (promotion.getUsageCount() >= promotion.getMaxUsage()) {
            throw new BusinessRuleException(
                    "Promotion usage limit has been reached");
        }

        return promotion;
    }

    public Reservation createReservationEntity(ReservationCreateRequest request, Client client) {

        Reservation reservation = reservationMapper.toEntity(request);

        Instant reservationDate = Instant.now();

        reservation.setReservationDate(reservationDate);
        reservation.setExpirationDate(reservationDate.plus(10, ChronoUnit.MINUTES));
        reservation.setClient(client);
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);

        return reservation;
    }

    public List<ReservationLine> createReservationLines(Reservation reservation, List<ReservationLineRequest> requests) {

        List<ReservationLine> lines = new ArrayList<>();

        for (ReservationLineRequest lineRequest : requests) {
            TicketCategoriesPerEvent category = findTicketCategory(lineRequest.getTicketCategoriesPerEventId());
            category.setAvailableQuantity(category.getAvailableQuantity() - lineRequest.getQuantity());
            ReservationLine line = new ReservationLine(reservation, category, lineRequest.getQuantity(), category.getUnitPrice());
            lines.add(line);
        }

        return lines;
    }

    public BigDecimal calculateTotalAmount(List<ReservationLine> lines, Promotion promotion) {

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (ReservationLine line : lines) {
            BigDecimal lineTotal = line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);
        }

        if (promotion == null) {
            return totalAmount;
        }

        if (promotion.getDiscountType() == DiscountType.PERCENTAGE) {
            return AmountCalculate.calculateFinalAmount(totalAmount, promotion.getDiscountValue());
        }

        if (totalAmount.compareTo(promotion.getDiscountValue()) < 0) {
            throw new BusinessRuleException(
                    "The discount is bigger than the total amount");
        }

        return totalAmount.subtract(promotion.getDiscountValue());
    }


    @Transactional
    public ReservationResponse cancelReservation(UUID reservationId, UUID clientId){
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        if (!reservation.getClient().getId().equals(clientId)){
            throw new BusinessRuleException("The reservation must be owned by the you to cancel it ");
        }

        if (reservation.getStatus() != ReservationStatus.PENDING_PAYMENT) {
            throw new BusinessRuleException(
                    "Only pending payment reservations can be canceled"
            );
        }

        reservation.setStatus(ReservationStatus.CANCELED);

        for (ReservationLine line : reservation.getReservationLines()){
            TicketCategoriesPerEvent category = line.getEventCategory();
            category.setAvailableQuantity(category.getAvailableQuantity() + line.getQuantity());
        }

        return reservationMapper.toResponse(reservation);
    }


    @Transactional
    public void expireReservation(UUID reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (reservation.getStatus() != ReservationStatus.PENDING_PAYMENT) {
            return;
        }

        if (Instant.now().isBefore(reservation.getExpirationDate())) {
            return;
        }

        reservation.setStatus(ReservationStatus.EXPIRED);

        for (ReservationLine line : reservation.getReservationLines()) {

            TicketCategoriesPerEvent category = line.getEventCategory();

            category.setAvailableQuantity(
                    category.getAvailableQuantity() + line.getQuantity()
            );
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expirePendingReservations() {

        List<Reservation> expiredReservations = reservationRepository.findByStatusAndExpirationDateLessThanEqual(ReservationStatus.PENDING_PAYMENT, Instant.now());

        for (Reservation reservation : expiredReservations) {
            expireReservation(reservation.getId());
        }
    }



    public ReservationResponse getReservationConfirmeParEmail(String email, String categoryName, int qteMin, UUID eventId){
       Optional<Reservation> reservationOp =  reservationRepository.getRservationConfirmer(email,categoryName,qteMin,eventId);
        if (reservationOp.isEmpty()){
            throw new AucuneReservationTrouveeException();
        }

        Reservation reservation = reservationOp.get();
        return reservationMapper.toResponse(reservation);



    }
}

