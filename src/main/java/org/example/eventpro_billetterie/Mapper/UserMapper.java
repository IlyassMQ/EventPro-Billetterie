package org.example.eventpro_billetterie.Mapper;


import org.example.eventpro_billetterie.DTO.Response.UserResponse;
import org.example.eventpro_billetterie.Entity.Client;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
    UserResponse toResponse(Organizer organizer);
    UserResponse toResponse(Client Client);

}
