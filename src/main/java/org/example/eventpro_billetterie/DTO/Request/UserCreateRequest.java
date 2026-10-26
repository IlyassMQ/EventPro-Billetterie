package org.example.eventpro_billetterie.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateRequest {

        @NotBlank(message = "User Name is required")
        private String userName;
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        private String email;
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must contain at least 6 characters")
        private String password;
        @Size(min = 10,message = "phone must contain at least 10 characters")
        private String phone;

        private String organizationName;
}
