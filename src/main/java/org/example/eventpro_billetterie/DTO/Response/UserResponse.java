package org.example.eventpro_billetterie.DTO.Response;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResponse {


        private UUID id;
        private String userName;
        private String email;
        private String organisationName;
        private String phone;

}
