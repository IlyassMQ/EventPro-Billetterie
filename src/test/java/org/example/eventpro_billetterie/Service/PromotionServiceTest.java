
package org.example.eventpro_billetterie.Service;

import org.example.eventpro_billetterie.DTO.Request.PromotionCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.PromotionResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Entity.Promotion;
import org.example.eventpro_billetterie.Entity.Enums.DiscountType;
import org.example.eventpro_billetterie.Mapper.PromotionMapper;
import org.example.eventpro_billetterie.Repository.EventRepository;
import org.example.eventpro_billetterie.Repository.PromotionRepository;
import org.example.eventpro_billetterie.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PromotionMapper promotionMapper;

    @InjectMocks
    private PromotionService promotionService;

    @Test
    void shouldCreatePromotion() {

        UUID organizerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);

        PromotionCreateRequest request = PromotionCreateRequest.builder()
                .code("SUMMER20")
                .eventId(eventId)
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(new BigDecimal("20"))
                .startDate(startDate)
                .endDate(endDate)
                .maxUsage(100)
                .build();

        Organizer organizer = new Organizer("Ahmed", "ahmed@gmail.com", "organisation");

        ReflectionTestUtils.setField(organizer, "id", organizerId);

        Event event = new Event();
        event.setOrganizer(organizer);

        Promotion promotion = new Promotion(
                event,
                "SUMMER20",
                DiscountType.PERCENTAGE,
                new BigDecimal("20"),
                startDate,
                endDate,
                100
        );

        Promotion savedPromotion = new Promotion(
                event,
                "SUMMER20",
                DiscountType.PERCENTAGE,
                new BigDecimal("20"),
                startDate,
                endDate,
                100
        );

        PromotionResponse expectedResponse = PromotionResponse.builder()
                .code("SUMMER20")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(new BigDecimal("20"))
                .startDate(startDate)
                .endDate(endDate)
                .maxUsage(100)
                .build();

        when(userRepository.findById(organizerId))
                .thenReturn(Optional.of(organizer));

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(promotionRepository.existsByEventIdAndCodeIgnoreCase(eventId, "SUMMER20"))
                .thenReturn(false);

        when(promotionMapper.toEntity(request))
                .thenReturn(promotion);

        when(promotionRepository.save(promotion))
                .thenReturn(savedPromotion);

        when(promotionMapper.toResponse(savedPromotion))
                .thenReturn(expectedResponse);


        PromotionResponse result =
                promotionService.createPromotion(request, organizerId);


        assertNotNull(result);
        assertEquals("SUMMER20", result.getCode());
        assertEquals(DiscountType.PERCENTAGE, result.getDiscountType());
        assertEquals(new BigDecimal("20"), result.getDiscountValue());

        assertEquals(event, promotion.getEvent());

        verify(userRepository).findById(organizerId);
        verify(eventRepository).findById(eventId);
        verify(promotionRepository).existsByEventIdAndCodeIgnoreCase(eventId, "SUMMER20");
        verify(promotionMapper).toEntity(request);
        verify(promotionRepository).save(promotion);
        verify(promotionMapper).toResponse(savedPromotion);
    }
}
