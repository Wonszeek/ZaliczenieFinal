
package com.example.demo.Restaurant.Service;

import com.example.demo.Restaurant.DataObject.ReservationRequest;
import com.example.demo.Restaurant.Domain.User;
import com.example.demo.Restaurant.Domain.Reservation;

import com.example.demo.Restaurant.Repository.ReservationRepository;
import com.example.demo.Restaurant.Domain.RestaurantTable;
import com.example.demo.Restaurant.Repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Transactional
    public Reservation makeReservation(User user, ReservationRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new IllegalArgumentException("Stolik nie istnieje"));


        if (isTableAvailable(table, request.getDate(), request.getStartTime(), request.getEndTime())) {
            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setRestaurantTable(table);
            reservation.setReservationDate(request.getDate());
            reservation.setReservationTime(request.getStartTime());
            reservation.setReservationEndTime(request.getEndTime());

            return reservationRepository.save(reservation);
        } else {
            throw new IllegalStateException("Stolik jest już zarezerwowany w podanym terminie");
        }
    }

    private boolean isTableAvailable(RestaurantTable table, LocalDate date,
                                     LocalTime startTime, LocalTime endTime) {
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                table, date, startTime, endTime);
        return conflicts.isEmpty();
    }

    public List<RestaurantTable> findAvailableTables(int people, LocalDate date,
                                                     LocalTime startTime, LocalTime endTime) {
        List<RestaurantTable> tables = tableRepository.findBySize(people);
        return tables.stream()
                .filter(table -> isTableAvailable(table, date, startTime, endTime))
                .toList();
    }

    public boolean isReservationValid(ReservationRequest request) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            return false;
        }
        Duration duration = Duration.between(request.getStartTime(), request.getEndTime());
        return !duration.minusMinutes(30).isNegative();
    }
}