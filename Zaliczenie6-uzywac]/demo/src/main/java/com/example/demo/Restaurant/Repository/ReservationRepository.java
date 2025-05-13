// ReservationRepository.java
package com.example.demo.Restaurant.Repository;

import com.example.demo.Restaurant.Domain.Reservation;
import com.example.demo.Restaurant.Domain.RestaurantTable;
import com.example.demo.Restaurant.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.restaurantTable = :table AND r.reservationDate = :date AND " +
            "((r.reservationTime <= :endTime AND r.reservationEndTime >= :startTime))")
    List<Reservation> findConflictingReservations(
            @Param("table") RestaurantTable table,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);


        List<Reservation> findByUserAndReservationDateGreaterThanEqualOrderByReservationDateAsc(
                User user, LocalDate date);

        List<Reservation> findByUserAndReservationDateLessThanOrderByReservationDateDesc(
                User user, LocalDate date);


    }
