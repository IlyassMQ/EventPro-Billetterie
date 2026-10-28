package org.example.eventpro_billetterie.Service;

import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.PromotionCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.PromotionResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.Enums.DiscountType;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Entity.Promotion;
import org.example.eventpro_billetterie.Entity.User;
import org.example.eventpro_billetterie.Exception.*;
import org.example.eventpro_billetterie.Mapper.PromotionMapper;
import org.example.eventpro_billetterie.Repository.EventRepository;
import org.example.eventpro_billetterie.Repository.PromotionRepository;
import org.example.eventpro_billetterie.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PromotionMapper promotionMapper;

    @Transactional
    public PromotionResponse createPromotion(PromotionCreateRequest request, UUID organizerId) {

        User user = userRepository.findById(organizerId)
                .orElseThrow(UserNotFound::new);

        if (!(user instanceof Organizer organizer)) {
            throw new IllegalArgumentException("The user is not an organizer");
        }
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(EventNotFound::new);

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new ResourceForbiddenException(
                    "You cannot create a promotion for this event"
            );
        }


        if (!request.getStartDate().isBefore(request.getEndDate())) {
            throw new BusinessRuleException(
                    "Promotion start date must be before its end date"
            );
        }

        if (request.getDiscountType() == DiscountType.PERCENTAGE && request.getDiscountValue().compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessRuleException(
                    "Percentage discount cannot exceed 100%"
            );
        }

        if (promotionRepository.existsByEventIdAndCodeIgnoreCase(request.getEventId(), request.getCode())) {
            throw new PromotionAlreadyExistsException();
        }

        Promotion promotion = promotionMapper.toEntity(request);
        promotion.setEvent(event);

        Promotion savedPromotion = promotionRepository.save(promotion);

        return promotionMapper.toResponse(savedPromotion);
    }

    public List<PromotionResponse> getPromotionsByEvent(UUID eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFound();
        }
        List<Promotion> promotions = promotionRepository.findByEventId(eventId);
        return promotions.stream().map(promotionMapper::toResponse)
                .toList();
    }

    public PromotionResponse getPromotionById(UUID promotionId) {

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(PromotionNotFound::new);

        return promotionMapper.toResponse(promotion);
    }

    public PromotionResponse getPromotionByCode(String promotionCode){

        Promotion promotion = promotionRepository.findByCode(promotionCode)
                .orElseThrow(PromotionNotFound::new);

        return promotionMapper.toResponse(promotion);
    }
}