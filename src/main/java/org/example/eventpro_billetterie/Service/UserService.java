package org.example.eventpro_billetterie.Service;

import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.UserCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.UserResponse;
import org.example.eventpro_billetterie.Entity.Admin;
import org.example.eventpro_billetterie.Entity.Client;
import org.example.eventpro_billetterie.Entity.ControlAgent;
import org.example.eventpro_billetterie.Entity.Organizer;
import org.example.eventpro_billetterie.Entity.User;
import org.example.eventpro_billetterie.Mapper.UserMapper;
import org.example.eventpro_billetterie.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    private UserResponse saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already exists");
        }

        User savedUser = userRepository.save(user);

        if (savedUser instanceof Organizer organizer) {
            return userMapper.toResponse(organizer);
        }

        if (savedUser instanceof Client client) {
            return userMapper.toResponse(client);
        }

        return userMapper.toResponse(savedUser);
    }


    @Transactional
    public UserResponse createAdmin(UserCreateRequest request) {
        Admin admin = new Admin(request.getUserName(), request.getEmail());
        return saveUser(admin);
    }

    @Transactional
    public UserResponse createOrganizer(UserCreateRequest request) {
        if (request.getOrganisationName() == null || request.getOrganisationName().isBlank()) {
            throw new IllegalArgumentException(
                    "Organization name is required");
        }

        Organizer organizer = new Organizer(request.getUserName(), request.getEmail(), request.getOrganisationName());

        return saveUser(organizer);
    }

    @Transactional
    public UserResponse createClient(UserCreateRequest request) {
        if (request.getPhone() == null || request.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        Client client = new Client(request.getUserName(), request.getEmail(), request.getPhone());

        return saveUser(client);
    }

    @Transactional
    public UserResponse createControlAgent(UserCreateRequest request) {
        ControlAgent agent = new ControlAgent(request.getUserName(), request.getEmail());

        return saveUser(agent);
    }

}