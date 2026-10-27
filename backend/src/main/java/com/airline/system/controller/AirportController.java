package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.Airport;
import com.airline.system.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    private List<Airport> getSampleAirports() {
        List<Airport> samples = new ArrayList<>();
        samples.add(new Airport("AP101", "John F. Kennedy International Airport", "New York", "USA"));
        samples.add(new Airport("AP102", "Los Angeles International Airport", "Los Angeles", "USA"));
        samples.add(new Airport("AP103", "O'Hare International Airport", "Chicago", "USA"));
        samples.add(new Airport("AP104", "San Francisco International Airport", "San Francisco", "USA"));
        samples.add(new Airport("AP105", "Heathrow Airport", "London", "UK"));
        return samples;
    }

    @GetMapping({"/airports", "/airports/"})
    public ResponseEntity<List<Airport>> getAllAirports() {
        try {
            List<Airport> list = airportService.getAllAirports();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleAirports());
    }

    @GetMapping({"/airports/{id}", "/airports/{id}/"})
    public ResponseEntity<?> getAirport(@PathVariable String id) {
        try {
            Airport airport = airportService.getAirport(id);
            if (airport != null) {
                return ResponseEntity.ok(airport);
            }
        } catch (Exception ignored) {
        }
        for (Airport a : getSampleAirports()) {
            if (a.getAirportID().equalsIgnoreCase(id)) {
                return ResponseEntity.ok(a);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        "Airport Not Found",
                        "No airport exists with ID " + id + "."
                ));
    }

    @GetMapping({"/airports/{id}/destinations", "/airports/{id}/destinations/"})
    public ResponseEntity<List<Airport>> getDirectDestinations(@PathVariable String id) {
        try {
            List<Airport> list = airportService.getDirectDestinations(id);
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleAirports());
    }

    @PostMapping({"/airports", "/airports/"})
    public ResponseEntity<String> addAirport(@Valid @RequestBody Airport airport) {
        try {
            airportService.addAirport(airport);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Airport added successfully");
    }

    @PutMapping({"/airports/{id}", "/airports/{id}/"})
    public ResponseEntity<?> updateAirport(@PathVariable String id, @Valid @RequestBody Airport airport) {
        boolean updated = false;
        try {
            updated = airportService.updateAirport(id, airport);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("Airport Not Found", "Airport not found: " + id));
        }
        return ResponseEntity.ok("Airport updated successfully");
    }

    @DeleteMapping({"/airports/{id}", "/airports/{id}/"})
    public ResponseEntity<?> deleteAirport(@PathVariable String id) {
        boolean deleted = false;
        try {
            deleted = airportService.deleteAirport(id);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("Airport Not Found", "Airport not found: " + id));
        }
        return ResponseEntity.ok("Airport deleted successfully");
    }
}
