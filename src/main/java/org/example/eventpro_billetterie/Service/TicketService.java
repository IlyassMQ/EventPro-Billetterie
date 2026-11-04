package org.example.eventpro_billetterie.Service;

import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Response.TicketResponse;
import org.example.eventpro_billetterie.Entity.*;
import org.example.eventpro_billetterie.Entity.Enums.TicketStatus;
import org.example.eventpro_billetterie.Entity.Enums.ReservationStatus;
import org.example.eventpro_billetterie.Exception.AgentNotFound;
import org.example.eventpro_billetterie.Exception.BusinessRuleException;
import org.example.eventpro_billetterie.Mapper.TicketMapper;
import org.example.eventpro_billetterie.Repository.TicketRepository;
import org.example.eventpro_billetterie.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;

    @Transactional
    public List<TicketResponse> createTickets(Reservation reservation) {

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BusinessRuleException(
                    "Tickets can only generated for confirmed reservations"
            );
        }

        List<Ticket> tickets = new ArrayList<>();

        for (ReservationLine line : reservation.getReservationLines()) {

            for (int i = 0; i < line.getQuantity(); i++) {

                Ticket ticket = new Ticket(line, UUID.randomUUID().toString(), Instant.now(), TicketStatus.VALID);
                tickets.add(ticket);
            }
        }

        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);

        return savedTickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }


    public List<TicketResponse> getClientTickets(UUID clientId) {

        List<Ticket> tickets = ticketRepository.findTicketsByClientId(clientId);

        return tickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    @Transactional
    public TicketResponse validateTicket(String ticketCode,UUID agentId) {
        isAgent(agentId);

        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new BusinessRuleException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.VALID) {
            throw new BusinessRuleException("This ticket is already used or canceled");
        }

        ticket.setStatus(TicketStatus.USED);
        ticket.setUsedAt(Instant.now());

        Ticket savedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(savedTicket);
    }

    private void isAgent(UUID agentId){

        User user = userRepository.findById(agentId)
                .orElseThrow(AgentNotFound::new);
        if (!(user instanceof ControlAgent)) {
            throw new BusinessRuleException(
                    "Only a ControlAgent can validate the ticket"
            );
        }
    }



}