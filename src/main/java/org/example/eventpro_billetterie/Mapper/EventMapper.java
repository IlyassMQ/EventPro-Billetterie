package org.example.eventpro_billetterie.Mapper;


import org.example.eventpro_billetterie.DTO.Request.EventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.EventResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
        TicketCategoriesPerEventMapper.class,
        PromotionMapper.class
        }
)
public interface EventMapper {

    @Mapping(target = "categories", source = "eventCategories")
    @Mapping(target = "promotions", source = "promotions")
    EventResponse toResponse(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "eventCategories", ignore = true)
    @Mapping(target = "promotions", ignore = true)
    @Mapping(target = "status", ignore = true)
    Event toEntity(EventCreateRequest creationDto);

}
