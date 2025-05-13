
package com.example.demo.Restaurant.DataObject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequest {
    @NotNull(message = "ID stolika jest wymagane")
    private Long tableId;

    @NotNull(message = "Data rezerwacji jest wymagana")
    private LocalDate date;

    @NotNull(message = "Godzina rozpoczęcia jest wymagana")
    private LocalTime startTime;

    @NotNull(message = "Godzina zakończenia jest wymagana")
    private LocalTime endTime;

    @Min(value = 1, message = "Minimum 1 osoba")
    @Max(value = 8, message = "Maksymalnie 8 osób")
    private int people;
}