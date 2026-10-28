package org.example.eventpro_billetterie.Service;


import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.TicketCategoryCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.TicketCategoryResponse;
import org.example.eventpro_billetterie.Entity.TicketCategory;
import org.example.eventpro_billetterie.Exception.TicketCategoryNotFound;
import org.example.eventpro_billetterie.Mapper.TicketCategoryMapper;
import org.example.eventpro_billetterie.Repository.TicketCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketCategoryService {

        private final TicketCategoryRepository ticketCategoryRepository;

        private final TicketCategoryMapper ticketCategoryMapper;

    @Transactional
    public TicketCategoryResponse creatTicketCategory(TicketCategoryCreateRequest request){
        TicketCategory ticketCategory = ticketCategoryMapper.toEntity(request);
        TicketCategory savedTicketCategory = ticketCategoryRepository.save(ticketCategory);

        return ticketCategoryMapper.toResponse(savedTicketCategory);
    }

    public List<TicketCategoryResponse> getAllTicketCategory(){
        return ticketCategoryRepository.findAll().stream().
                map(ticketCategoryMapper::toResponse).toList();
    }

    public TicketCategoryResponse getTicketCategoryById(UUID id){
        TicketCategory ticketCategory = ticketCategoryRepository.findById(id).orElseThrow(TicketCategoryNotFound::new);

        return ticketCategoryMapper.toResponse(ticketCategory);
    }

}
