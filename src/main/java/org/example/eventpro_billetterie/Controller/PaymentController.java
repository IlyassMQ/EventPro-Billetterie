package org.example.eventpro_billetterie.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.PaymentCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.PaymentResponse;
import org.example.eventpro_billetterie.Service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{clientId}")
    public PaymentResponse createPayment(@Valid @RequestBody PaymentCreateRequest request,@PathVariable("clientId") UUID clientId){
        return paymentService.createPayment(request,clientId);
    }
}