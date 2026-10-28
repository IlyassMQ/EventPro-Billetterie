
package org.example.eventpro_billetterie.Mapper;

import org.example.eventpro_billetterie.DTO.Request.ReservationCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.ReservationResponse;
import org.example.eventpro_billetterie.DTO.Response.ReservationDetailsResponse;
import org.example.eventpro_billetterie.Entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ReservationLineMapper.class)
public interface ReservationMapper {

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "eventTitle", source = "event.title")
    ReservationResponse toResponse(Reservation reservation);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "eventTitle", source = "event.title")
    @Mapping(target = "reservationLines", source = "reservationLines")
    @Mapping(target = "createdAt", ignore = true)
    ReservationDetailsResponse toDetailsResponse(Reservation reservation);

    Reservation toEntity(ReservationCreateRequest request);
}
