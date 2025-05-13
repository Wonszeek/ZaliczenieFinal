package com.example.demo.Restaurant.Domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_table_id", nullable = false)
    private RestaurantTable restaurantTable;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "reservation_time", nullable = false)
    private LocalTime reservationTime;

    @Column(name = "reservation_end_time", nullable = false)
    private LocalTime reservationEndTime;

    // Constructors
    public Reservation() {}

    public Reservation(User user, RestaurantTable restaurantTable,
                       LocalDate reservationDate, LocalTime reservationTime,
                       LocalTime reservationEndTime) {
        this.user = user;
        this.restaurantTable = restaurantTable;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.reservationEndTime = reservationEndTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalTime getReservationEndTime() {
        return reservationEndTime;
    }

    public void setReservationEndTime(LocalTime reservationEndTime) {
        this.reservationEndTime = reservationEndTime;
    }
}