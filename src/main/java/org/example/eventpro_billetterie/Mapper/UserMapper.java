package org.example.eventpro_billetterie.Mapper;


import org.example.eventpro_billetterie.DTO.Request.UserCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.UserResponse;
import org.example.eventpro_billetterie.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserResponse toResponse(User user);


    User toEntity(UserCreateRequest userCreateRequest);
}
