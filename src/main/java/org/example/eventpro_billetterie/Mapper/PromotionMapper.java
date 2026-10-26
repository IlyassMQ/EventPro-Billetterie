package org.example.eventpro_billetterie.Mapper;


import org.example.eventpro_billetterie.DTO.Request.PromotionCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.PromotionResponse;
import org.example.eventpro_billetterie.Entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    PromotionResponse toResponse(Promotion promotion);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "event",ignore = true)
    Promotion toEntity(PromotionCreateRequest createDto);

}
