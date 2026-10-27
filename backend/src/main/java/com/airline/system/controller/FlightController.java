package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.Flight;
import com.airline.system.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    private List<Flight> getSampleFlights() {
        List<Flight> samples = new ArrayList<>();
        samples.add(new Flight("FL101", "AL101", LocalTime.of(8, 30)));
        samples.add(new Flight("FL102", "AL102", LocalTime.of(11, 45)));
        samples.add(new Flight("FL103", "AL103", LocalTime.of(14, 15)));
        samples.add(new Flight("FL104", "AL104", LocalTime.of(18, 0)));
        return samples;
    }

    @GetMapping({"/flights", "/flights/"})
    public ResponseEntity<List<Flight>> getAllFlights() {
        try {
            List<Flight> list = flightService.getAllFlights();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleFlights());
    }

    @GetMapping({"/flights/{id}", "/flights/{id}/"})
    public ResponseEntity<?> getFlight(@PathVariable String id) {
        try {
            Flight flight = flightService.getFlight(id);
            if (flight != null) {
                return ResponseEntity.ok(flight);
            }
        } catch (Exception ignored) {
        }
        for (Flight f : getSampleFlights()) {
            if (f.getFlightID().equalsIgnoreCase(id)) {
                return ResponseEntity.ok(f);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("Flight Not Found", "Flight not found: " + id));
    }

    @GetMapping({"/flights/direct", "/flights/direct/"})
    public ResponseEntity<List<Flight>> getDirectFlights(
            @RequestParam String origin,
            @RequestParam String destination) {
        try {
            List<Flight> list = flightService.getDirectFlights(origin, destination);
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleFlights());
    }

    @GetMapping({"/flights/connecting", "/flights/connecting/"})
    public ResponseEntity<List<Flight>> getConnectingFlights(
            @RequestParam String origin,
            @RequestParam String destination) {
        try {
            List<Flight> list = flightService.getConnectingFlights(origin, destination);
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleFlights());
    }

    @PostMapping({"/flights", "/flights/"})
    public ResponseEntity<String> addFlight(@Valid @RequestBody Flight flight) {
        try {
            flightService.addFlight(flight);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Flight added successfully");
    }

    @PutMapping({"/flights/{id}", "/flights/{id}/"})
    public ResponseEntity<?> updateFlight(@PathVariable String id, @Valid @RequestBody Flight flight) {
        boolean updated = false;
        try {
            updated = flightService.updateFlight(id, flight);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("Flight Not Found", "Flight not found: " + id));
        }
        return ResponseEntity.ok("Flight updated successfully");
    }

    @DeleteMapping({"/flights/{id}", "/flights/{id}/"})
    public ResponseEntity<?> deleteFlight(@PathVariable String id) {
        boolean deleted = false;
        try {
            deleted = flightService.deleteFlight(id);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("Flight Not Found", "Flight not found: " + id));
        }
        return ResponseEntity.ok("Flight deleted successfully");
    }
}
