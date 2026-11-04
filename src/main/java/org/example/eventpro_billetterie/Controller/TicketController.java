package org.example.eventpro_billetterie.Controller;

import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Response.TicketResponse;
import org.example.eventpro_billetterie.Service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/client/{clientId}")
    public List<TicketResponse> getClientTickets(@PathVariable UUID clientId) {
        return ticketService.getClientTickets(clientId);
    }

    @PatchMapping("/{agentId}/validate/{ticketCode}")
    public TicketResponse validateTicket(@PathVariable String ticketCode,@PathVariable UUID agentId) {
        return ticketService.validateTicket(ticketCode,agentId);
    }
}