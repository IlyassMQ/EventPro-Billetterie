
package org.example.eventpro_billetterie.Mapper;

import org.example.eventpro_billetterie.DTO.Response.RefundResponse;
import org.example.eventpro_billetterie.Entity.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefundMapper {

    @Mapping(target = "paymentId", source = "payment.id")
    RefundResponse toResponse(Refund refund);
}
