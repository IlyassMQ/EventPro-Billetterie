package org.example.eventpro_billetterie.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.RefundCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.RefundResponse;
import org.example.eventpro_billetterie.Service.RefundService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping("/{clientId}")
    public RefundResponse createRefund(@Valid @RequestBody RefundCreateRequest request, @PathVariable UUID clientId) {
        return refundService.createRefund(request, clientId);
    }

    @PatchMapping("/{adminId}/approve/{refundId}")
    public RefundResponse approveRefund(@PathVariable UUID refundId, @PathVariable UUID adminId) {
        return refundService.approveRefund(refundId, adminId);
    }

    @PatchMapping("/{adminId}/reject/{refundId}")
    public RefundResponse rejectRefund(@PathVariable UUID refundId, @PathVariable UUID adminId) {
        return refundService.rejectRefund(refundId, adminId);
    }
}