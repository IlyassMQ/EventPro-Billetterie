package org.example.eventpro_billetterie.Mapper;

import org.example.eventpro_billetterie.DTO.Response.ReservationLineResponse;
import org.example.eventpro_billetterie.Entity.ReservationLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationLineMapper {

    @Mapping(target = "ticketCategoriesPerEventId", source = "eventCategory.id")
    @Mapping(target = "categoryName", source = "eventCategory.category.name")
    ReservationLineResponse toResponse(ReservationLine reservationLine);
}