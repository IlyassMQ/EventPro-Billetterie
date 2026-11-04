package org.example.eventpro_billetterie.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.TicketCategoryCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.TicketCategoryResponse;
import org.example.eventpro_billetterie.Service.TicketCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket-categories")
@RequiredArgsConstructor
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketCategoryResponse createTicketCategory(@Valid @RequestBody TicketCategoryCreateRequest request){
        return ticketCategoryService.creatTicketCategory(request);
    }

    @GetMapping("/{id}")
    public TicketCategoryResponse getTicketCategoryById(@PathVariable UUID id) {
        return ticketCategoryService.getTicketCategoryById(id);
    }

    @GetMapping
    public List<TicketCategoryResponse> getAllEvents() {
        return ticketCategoryService.getAllTicketCategory();
    }
}
