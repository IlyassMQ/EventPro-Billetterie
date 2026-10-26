package org.example.eventpro_billetterie.Mapper;


import org.example.eventpro_billetterie.DTO.Request.EventCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.EventResponse;
import org.example.eventpro_billetterie.Entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventResponse toResponse(Event event);

    @Mapping(target = "id", ignore = true)
    Event toEntity(EventCreateRequest creationDto);

}
