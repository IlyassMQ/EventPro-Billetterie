package org.example.eventpro_billetterie.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.UserCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.UserResponse;
import org.example.eventpro_billetterie.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createAdmin(@Valid @RequestBody UserCreateRequest request){
        return userService.createAdmin(request);
    }


    @PostMapping("/orgnizer")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createOrganizer(@Valid @RequestBody UserCreateRequest request){
        return userService.createOrganizer(request);
    }

    @PostMapping("/client")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createClient(@Valid @RequestBody UserCreateRequest request){
        return userService.createClient(request);
    }

    @PostMapping("/controlAgent")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createControleAgent(@Valid @RequestBody UserCreateRequest request){
        return userService.createControlAgent(request);
    }



}
