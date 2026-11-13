
package org.example.eventpro_billetterie.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventpro_billetterie.DTO.Request.ReservationCreateRequest;
import org.example.eventpro_billetterie.DTO.Response.ReservationResponse;
import org.example.eventpro_billetterie.Entity.Reservation;
import org.example.eventpro_billetterie.Service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{clientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(@PathVariable UUID clientId, @Valid @RequestBody ReservationCreateRequest request) {

        return reservationService.createReservation(request, clientId);
    }

    @PatchMapping("/{reservationId}/cancel/{clientId}")
    public ReservationResponse cancelReservation(@PathVariable UUID reservationId, @PathVariable UUID clientId) {

        return reservationService.cancelReservation(reservationId, clientId);
    }

    @GetMapping("/recherche")
    public ReservationResponse rechecheReservation(@RequestParam String email,@RequestParam String categoryName,@RequestParam int qteMin,@RequestParam UUID eventId){
        return reservationService.getReservationConfirmeParEmail(email,categoryName,qteMin,eventId);
    }


}
