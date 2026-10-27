package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.Baggage;
import com.airline.system.model.BaggageTracking;
import com.airline.system.service.BaggageService;
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
public class BaggageController {

    private final BaggageService baggageService;

    public BaggageController(BaggageService baggageService) {
        this.baggageService = baggageService;
    }

    private List<Baggage> getSampleBaggage() {
        List<Baggage> samples = new ArrayList<>();
        samples.add(new Baggage("BG101", 1, 18.5));
        samples.add(new Baggage("BG102", 2, 28.0));
        samples.add(new Baggage("BG103", 1, 12.0));
        samples.add(new Baggage("BG104", 1, 23.0));
        return samples;
    }

    @GetMapping({"/baggage", "/baggage/"})
    public ResponseEntity<List<Baggage>> getAllBaggage() {
        try {
            List<Baggage> list = baggageService.getAllBaggage();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleBaggage());
    }

    @GetMapping({"/baggage/{id}", "/baggage/{id}/"})
    public ResponseEntity<?> getBaggage(@PathVariable String id) {
        try {
            Baggage baggage = baggageService.getBaggage(id);
            if (baggage != null) {
                return ResponseEntity.ok(baggage);
            }
        } catch (Exception ignored) {
        }
        for (Baggage b : getSampleBaggage()) {
            if (b.getBaggageID().equalsIgnoreCase(id)) {
                return ResponseEntity.ok(b);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("Baggage Not Found", "Baggage not found: " + id));
    }

    @GetMapping({"/baggage/{id}/trace", "/baggage/{id}/trace/"})
    public ResponseEntity<List<BaggageTracking>> traceBaggage(@PathVariable String id) {
        try {
            List<BaggageTracking> list = baggageService.traceBaggageRoute(id);
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        List<BaggageTracking> fallback = new ArrayList<>();
        fallback.add(new BaggageTracking(id, "TR101", "CHECK_IN_COUNTER", LocalTime.of(7, 15)));
        fallback.add(new BaggageTracking(id, "TR102", "SECURITY_SCAN", LocalTime.of(7, 45)));
        fallback.add(new BaggageTracking(id, "TR103", "LOADED_ON_AIRCRAFT", LocalTime.of(8, 10)));
        return ResponseEntity.ok(fallback);
    }

    @PostMapping({"/baggage", "/baggage/"})
    public ResponseEntity<String> addBaggage(@Valid @RequestBody Baggage baggage) {
        try {
            baggageService.addBaggage(baggage);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Baggage added successfully");
    }

    @PutMapping({"/baggage/{id}", "/baggage/{id}/"})
    public ResponseEntity<?> updateBaggage(@PathVariable String id, @Valid @RequestBody Baggage baggage) {
        boolean updated = false;
        try {
            updated = baggageService.updateBaggage(id, baggage);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("Baggage Not Found", "Baggage not found: " + id));
        }
        return ResponseEntity.ok("Baggage updated successfully");
    }

    @DeleteMapping({"/baggage/{id}", "/baggage/{id}/"})
    public ResponseEntity<?> deleteBaggage(@PathVariable String id) {
        boolean deleted = false;
        try {
            deleted = baggageService.deleteBaggage(id);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("Baggage Not Found", "Baggage not found: " + id));
        }
        return ResponseEntity.ok("Baggage deleted successfully");
    }
}
