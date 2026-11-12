package org.example.eventpro_billetterie.Service;

import org.example.eventpro_billetterie.DTO.Request.UserCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.UserResponse;
import org.example.eventpro_billetterie.Entity.*;
import org.example.eventpro_billetterie.Mapper.UserMapper;
import org.example.eventpro_billetterie.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateAdmin() {
        UserCreateRequest request = UserCreateRequest.builder()
                .userName("Ahmed")
                .email("ahmed@gmail.com")
                .build();

        Admin admin = new Admin("Ahmed", "ahmed@gmail.com");

        UserResponse expectedResponse = UserResponse.builder()
                .userName("Ahmed")
                .email("ahmed@gmail.com")
                .build();

        when(userRepository.save(any(Admin.class))).thenReturn(admin);
        when(userMapper.toResponse(admin)).thenReturn(expectedResponse);

        UserResponse result = userService.createAdmin(request);

        assertNotNull(result);
        assertEquals("Ahmed", result.getUserName());
        assertEquals("ahmed@gmail.com", result.getEmail());

        verify(userRepository).save(any(Admin.class));
        verify(userMapper).toResponse(admin);
    }

    @Test
    void shouldCreateOrganizer() {
        UserCreateRequest request = UserCreateRequest.builder()
                .userName("Youssef")
                .email("youssef@gmail.com")
                .organisationName("EventPro")
                .build();

        Organizer organizer = new Organizer(
                "Youssef", "youssef@gmail.com", "EventPro"
        );

        UserResponse expectedResponse = UserResponse.builder()
                .userName("Youssef")
                .email("youssef@gmail.com")
                .build();

        when(userRepository.save(any(Organizer.class))).thenReturn(organizer);
        when(userMapper.toResponse(organizer)).thenReturn(expectedResponse);

        UserResponse result = userService.createOrganizer(request);

        assertNotNull(result);
        assertEquals("Youssef", result.getUserName());

        verify(userRepository).save(any(Organizer.class));
        verify(userMapper).toResponse(organizer);
    }

    @Test
    void shouldCreateClient() {
        UserCreateRequest request = UserCreateRequest.builder()
                .userName("Sara")
                .email("sara@gmail.com")
                .phone("0612345678")
                .build();

        Client client = new Client(
                "Sara", "sara@gmail.com", "0612345678"
        );

        UserResponse expectedResponse = UserResponse.builder()
                .userName("Sara")
                .email("sara@gmail.com")
                .build();

        when(userRepository.save(any(Client.class))).thenReturn(client);
        when(userMapper.toResponse(client)).thenReturn(expectedResponse);

        UserResponse result = userService.createClient(request);

        assertNotNull(result);
        assertEquals("Sara", result.getUserName());

        verify(userRepository).save(any(Client.class));
        verify(userMapper).toResponse(client);
    }

    @Test
    void shouldCreateControlAgent() {
        UserCreateRequest request = UserCreateRequest.builder()
                .userName("Omar")
                .email("omar@gmail.com")
                .build();

        ControlAgent agent = new ControlAgent("Omar", "omar@gmail.com");

        UserResponse expectedResponse = UserResponse.builder()
                .userName("Omar")
                .email("omar@gmail.com")
                .build();

        when(userRepository.save(any(ControlAgent.class))).thenReturn(agent);
        when(userMapper.toResponse(agent)).thenReturn(expectedResponse);

        UserResponse result = userService.createControlAgent(request);

        assertNotNull(result);
        assertEquals("Omar", result.getUserName());

        verify(userRepository).save(any(ControlAgent.class));
        verify(userMapper).toResponse(agent);
    }


    @Test
    void shouldThrowExceptionWhenOrganizationNameIsEmpty() {
        UserCreateRequest request = UserCreateRequest.builder()
                .userName("Youssef")
                .email("youssef@gmail.com")
                .organisationName("")
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createOrganizer(request)
        );

        assertEquals("Organization name is required", exception.getMessage());

        verifyNoInteractions(userRepository, userMapper);
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsEmpty() {
        UserCreateRequest request = UserCreateRequest.builder()
                .userName("Sara")
                .email("sara@gmail.com")
                .phone("")
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createClient(request)
        );

        assertEquals("Phone number is required", exception.getMessage());

        verifyNoInteractions(userRepository, userMapper);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserCreateRequest request = UserCreateRequest.builder()
                .userName("Ahmed")
                .email("ahmed@gmail.com")
                .build();

        when(userRepository.existsByEmail("ahmed@gmail.com"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createAdmin(request)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository).existsByEmail("ahmed@gmail.com");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(userMapper);
    }

}