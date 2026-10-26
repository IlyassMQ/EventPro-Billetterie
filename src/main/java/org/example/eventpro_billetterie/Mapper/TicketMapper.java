
package org.example.eventpro_billetterie.Mapper;

import org.example.eventpro_billetterie.DTO.Response.TicketResponse;
import org.example.eventpro_billetterie.Entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "reservationLineId", source = "reservationLine.id")
    TicketResponse toResponse(Ticket ticket);
}
