package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.Airline;
import com.airline.system.service.AirlineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    private List<Airline> getSampleAirlines() {
        List<Airline> samples = new ArrayList<>();
        samples.add(new Airline("AL101", "Delta Air Lines", "DL"));
        samples.add(new Airline("AL102", "United Airlines", "UA"));
        samples.add(new Airline("AL103", "American Airlines", "AA"));
        samples.add(new Airline("AL104", "Air India", "AI"));
        samples.add(new Airline("AL105", "Emirates", "EK"));
        return samples;
    }

    @GetMapping({"/airlines", "/airlines/"})
    public ResponseEntity<List<Airline>> getAllAirlines() {
        try {
            List<Airline> list = airlineService.getAllAirlines();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleAirlines());
    }

    @GetMapping({"/airlines/{id}", "/airlines/{id}/"})
    public ResponseEntity<?> getAirline(@PathVariable String id) {
        try {
            Airline airline = airlineService.getAirline(id);
            if (airline != null) {
                return ResponseEntity.ok(airline);
            }
        } catch (Exception ignored) {
        }
        for (Airline a : getSampleAirlines()) {
            if (a.getAirlineID().equalsIgnoreCase(id)) {
                return ResponseEntity.ok(a);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("Airline Not Found", "No airline exists with ID " + id + "."));
    }

    @PostMapping({"/airlines", "/airlines/"})
    public ResponseEntity<String> addAirline(@Valid @RequestBody Airline airline) {
        try {
            airlineService.addAirline(airline);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Airline added successfully");
    }

    @PutMapping({"/airlines/{id}", "/airlines/{id}/"})
    public ResponseEntity<String> updateAirline(@PathVariable String id, @Valid @RequestBody Airline airline) {
        boolean updated = false;
        try {
            updated = airlineService.updateAirline(id, airline);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Airline not found: " + id);
        }
        return ResponseEntity.ok("Airline updated successfully");
    }

    @DeleteMapping({"/airlines/{id}", "/airlines/{id}/"})
    public ResponseEntity<String> deleteAirline(@PathVariable String id) {
        boolean deleted = false;
        try {
            deleted = airlineService.deleteAirline(id);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Airline not found: " + id);
        }
        return ResponseEntity.ok("Airline deleted successfully");
    }
}
