
package org.example.eventpro_billetterie.Mapper;

import org.example.eventpro_billetterie.DTO.Request.TicketCategoryPerEventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.TicketCategoryPerEventResponse;
import org.example.eventpro_billetterie.Entity.TicketCategoriesPerEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketCategoriesPerEventMapper {

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "ticketCategoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    TicketCategoryPerEventResponse toResponse(TicketCategoriesPerEvent ticketCategoriesPerEvent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "category", ignore = true)
    TicketCategoriesPerEvent toEntity(TicketCategoryPerEventCreateRequest creationDto);
}
