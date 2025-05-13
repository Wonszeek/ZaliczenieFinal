
package com.example.demo.Restaurant.DataObject;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequest {
    private Long tableId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int people;
}