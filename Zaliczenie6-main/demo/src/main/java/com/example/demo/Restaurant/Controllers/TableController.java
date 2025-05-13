package com.example.demo.Restaurant.Controllers;

import com.example.demo.Restaurant.DataObject.AvailableTablesRequest;
import com.example.demo.Restaurant.DataObject.ReservationRequest;
import com.example.demo.Restaurant.Domain.RestaurantTable;
import com.example.demo.Restaurant.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "http://localhost:4200")
public class TableController {

    private final ReservationService reservationService;

    public TableController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/available")
    public ResponseEntity<List<RestaurantTable>> findAvailableTables(
            @RequestBody AvailableTablesRequest request) {
        List<RestaurantTable> availableTables = reservationService.findAvailableTables(
                request.getPeople(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );
        return ResponseEntity.ok(availableTables);
    }

}