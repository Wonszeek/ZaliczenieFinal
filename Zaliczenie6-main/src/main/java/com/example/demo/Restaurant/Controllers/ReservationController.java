package com.example.demo.Restaurant.Controllers;

import com.example.demo.Restaurant.DataObject.ReservationRequest;
import com.example.demo.Restaurant.Domain.Reservation;
import com.example.demo.Restaurant.Domain.RestaurantTable;
import com.example.demo.Restaurant.Domain.User;
import com.example.demo.Restaurant.Repository.ReservationRepository;
import com.example.demo.Restaurant.Service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }
    @GetMapping("/new")
    public String showReservationForm(
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

//        if (people == null) people = 2;
//        if (date == null) date = LocalDate.now();

        model.addAttribute("people", people);
        model.addAttribute("date", date);
        model.addAttribute("request", new ReservationRequest());

        return "reservation-form";
    }

    @PostMapping("/check-availability")
    public String checkAvailability(
            @ModelAttribute ReservationRequest request,
            Model model) {

        List<RestaurantTable> availableTables = reservationService.findAvailableTables(
                request.getPeople(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime());

        model.addAttribute("availableTables", availableTables);
        model.addAttribute("request", request);

        return "reservation-form";
    }
    @PostMapping
    public String makeReservation(
            @ModelAttribute ReservationRequest request,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {

        try {
            Reservation reservation = reservationService.makeReservation(user, request);
            redirectAttributes.addFlashAttribute("reservation", reservation);
            return "redirect:/reservations/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations/new";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage(Model model) {
        if (!model.containsAttribute("reservation")) {
            return "redirect:/reservations/new";
        }
        return "reservation-success";
    }
    @GetMapping("/my-reservations")
    public String getUserReservations(
            @AuthenticationPrincipal User user,
            Model model) {

        LocalDate today = LocalDate.now();


        List<Reservation> currentReservations = reservationRepository
                .findByUserAndReservationDateGreaterThanEqualOrderByReservationDateAsc(user, today);


        List<Reservation> pastReservations = reservationRepository
                .findByUserAndReservationDateLessThanOrderByReservationDateDesc(user, today);

        model.addAttribute("currentReservations", currentReservations);
        model.addAttribute("pastReservations", pastReservations);

        return "YourReservations";
    }
}

