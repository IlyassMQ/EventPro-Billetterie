package org.example.eventpro_billetterie.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.PromotionCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.PromotionResponse;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping("/events/promotions/{organizerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionResponse createPromotion(@Valid @RequestBody PromotionCreateRequest request,@PathVariable UUID organizerId) {

        return promotionService.createPromotion(request, organizerId);
    }

    @GetMapping("/events/{eventId}/promotions")
    public List<PromotionResponse> getPromotionsByEvent(@PathVariable UUID eventId) {

        return promotionService.getPromotionsByEvent(eventId);
    }

    @GetMapping("/promotions/{promotionId}")
    public PromotionResponse getPromotionById(@PathVariable UUID promotionId) {

        return promotionService.getPromotionById(promotionId);
    }
}
