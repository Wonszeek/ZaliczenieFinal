package com.example.demo.Restaurant.Controllers;

import com.example.demo.Restaurant.DataObject.ReservationRequest;
import com.example.demo.Restaurant.Domain.Reservation;
import com.example.demo.Restaurant.Domain.User;
import com.example.demo.Restaurant.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> makeReservation(@RequestBody ReservationRequest request) {
        try {
            if (!reservationService.isReservationValid(request)) {
                return ResponseEntity.badRequest().body("Nieprawidłowe dane rezerwacji");
            }

            Reservation reservation = reservationService.makeReservation(
                    getCurrentUser(), // You'll need to implement this
                    request
            );
            return ResponseEntity.ok(reservation);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Wystąpił błąd podczas rezerwacji");
        }
    }

    // Helper method - implement according to your auth system
    private User getCurrentUser() {
        // Get from security context or session
        // Example: return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        throw new UnsupportedOperationException("Implement user retrieval");
    }
}