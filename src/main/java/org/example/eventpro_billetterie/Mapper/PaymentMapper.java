
package org.example.eventpro_billetterie.Mapper;

import org.example.eventpro_billetterie.DTO.Response.PaymentResponse;
import org.example.eventpro_billetterie.Entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "reservationId", source = "reservation.id")
    PaymentResponse toResponse(Payment payment);
}
