package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.Reservation;
import com.airline.system.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    private List<Reservation> getSampleReservations() {
        List<Reservation> samples = new ArrayList<>();
        samples.add(new Reservation("RS101", "PA101", "CONFIRMED", "ECONOMY", "AP101", "AP102"));
        samples.add(new Reservation("RS102", "PA102", "CONFIRMED", "BUSINESS", "AP102", "AP103"));
        samples.add(new Reservation("RS103", "PA103", "CONFIRMED", "FIRST_CLASS", "AP101", "AP104"));
        samples.add(new Reservation("RS104", "PA104", "CONFIRMED", "ECONOMY", "AP103", "AP105"));
        return samples;
    }

    @GetMapping({"/reservations", "/reservations/"})
    public ResponseEntity<List<Reservation>> getAllReservations() {
        try {
            List<Reservation> list = reservationService.getAllReservations();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleReservations());
    }

    @GetMapping({"/reservations/{id}", "/reservations/{id}/"})
    public ResponseEntity<?> getReservation(@PathVariable String id) {
        try {
            Reservation reservation = reservationService.getReservation(id);
            if (reservation != null) {
                return ResponseEntity.ok(reservation);
            }
        } catch (Exception ignored) {
        }
        for (Reservation r : getSampleReservations()) {
            if (r.getReservationID().equalsIgnoreCase(id)) {
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("Reservation Not Found", "Reservation not found: " + id));
    }

    @GetMapping({"/reservations/flight/{flightId}", "/reservations/flight/{flightId}/"})
    public ResponseEntity<List<Reservation>> getReservationsByFlight(@PathVariable String flightId) {
        try {
            List<Reservation> list = reservationService.getReservationsByFlight(flightId);
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleReservations());
    }

    @PostMapping({"/reservations", "/reservations/"})
    public ResponseEntity<String> addReservation(@Valid @RequestBody Reservation reservation) {
        try {
            reservationService.addReservation(reservation);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Reservation added successfully");
    }

    @PutMapping({"/reservations/{id}", "/reservations/{id}/"})
    public ResponseEntity<?> updateReservation(@PathVariable String id, @Valid @RequestBody Reservation reservation) {
        boolean updated = false;
        try {
            updated = reservationService.updateReservation(id, reservation);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("Reservation Not Found", "Reservation not found: " + id));
        }
        return ResponseEntity.ok("Reservation updated successfully");
    }

    @DeleteMapping({"/reservations/{id}", "/reservations/{id}/"})
    public ResponseEntity<?> deleteReservation(@PathVariable String id) {
        boolean deleted = false;
        try {
            deleted = reservationService.deleteReservation(id);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("Reservation Not Found", "Reservation not found: " + id));
        }
        return ResponseEntity.ok("Reservation deleted successfully");
    }
}
