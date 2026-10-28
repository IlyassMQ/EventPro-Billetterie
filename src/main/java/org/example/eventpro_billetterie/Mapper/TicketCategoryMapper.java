
package org.example.eventpro_billetterie.Mapper;

import org.example.eventpro_billetterie.DTO.Request.TicketCategoryCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.TicketCategoryResponse;
import org.example.eventpro_billetterie.Entity.TicketCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketCategoryMapper {

    TicketCategoryResponse toResponse(TicketCategory ticketCategory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventCategories", ignore = true)
    TicketCategory toEntity(TicketCategoryCreateRequest creationDto);
}
