package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class UserUpdateRequest {

    @NotBlank(message = "User Name is required")
    private String userName;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
}
