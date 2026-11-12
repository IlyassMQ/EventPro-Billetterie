
package org.example.eventpro_billetterie.Service;

import org.example.eventpro_billetterie.DTO.Request.TicketCategoryCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.TicketCategoryResponse;
import org.example.eventpro_billetterie.Entity.TicketCategory;
import org.example.eventpro_billetterie.Exception.TicketCategoryNotFound;
import org.example.eventpro_billetterie.Mapper.TicketCategoryMapper;
import org.example.eventpro_billetterie.Repository.TicketCategoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketCategoryServiceTest {

    @Mock
    private TicketCategoryRepository ticketCategoryRepository;

    @Mock
    private TicketCategoryMapper ticketCategoryMapper;

    @InjectMocks
    private TicketCategoryService ticketCategoryService;


    @Test
    void shouldCreateTicketCategory() {

        TicketCategoryCreateRequest request = mock(TicketCategoryCreateRequest.class);
        TicketCategory category = mock(TicketCategory.class);
        TicketCategoryResponse response = mock(TicketCategoryResponse.class);

        when(ticketCategoryMapper.toEntity(request)).thenReturn(category);
        when(ticketCategoryRepository.save(category)).thenReturn(category);
        when(ticketCategoryMapper.toResponse(category)).thenReturn(response);

        TicketCategoryResponse result =
                ticketCategoryService.creatTicketCategory(request);

        assertNotNull(result);
        assertSame(response, result);

        verify(ticketCategoryRepository).save(category);
    }


    @Test
    void shouldGetAllTicketCategories() {

        TicketCategory category = mock(TicketCategory.class);
        TicketCategoryResponse response = mock(TicketCategoryResponse.class);

        when(ticketCategoryRepository.findAll())
                .thenReturn(List.of(category));

        when(ticketCategoryMapper.toResponse(category))
                .thenReturn(response);

        List<TicketCategoryResponse> result =
                ticketCategoryService.getAllTicketCategory();

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
    }


    @Test
    void shouldGetTicketCategoryById() {

        UUID categoryId = UUID.randomUUID();

        TicketCategory category = mock(TicketCategory.class);
        TicketCategoryResponse response = mock(TicketCategoryResponse.class);

        when(ticketCategoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(ticketCategoryMapper.toResponse(category))
                .thenReturn(response);

        TicketCategoryResponse result =
                ticketCategoryService.getTicketCategoryById(categoryId);

        assertNotNull(result);
        assertSame(response, result);
    }


    @Test
    void shouldThrowExceptionWhenTicketCategoryDoesNotExist() {

        UUID categoryId = UUID.randomUUID();

        when(ticketCategoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(
                TicketCategoryNotFound.class,
                () -> ticketCategoryService.getTicketCategoryById(categoryId)
        );
    }
}
