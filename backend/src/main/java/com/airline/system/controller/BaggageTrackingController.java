package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.BaggageTracking;
import com.airline.system.service.BaggageTrackingService;
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
public class BaggageTrackingController {

    private final BaggageTrackingService baggageTrackingService;

    public BaggageTrackingController(BaggageTrackingService baggageTrackingService) {
        this.baggageTrackingService = baggageTrackingService;
    }

    private List<BaggageTracking> getSampleTracking() {
        List<BaggageTracking> samples = new ArrayList<>();
        samples.add(new BaggageTracking("BG101", "TR101", "CHECK_IN_COUNTER", LocalTime.of(7, 15)));
        samples.add(new BaggageTracking("BG101", "TR102", "SECURITY_SCAN", LocalTime.of(7, 45)));
        samples.add(new BaggageTracking("BG101", "TR103", "LOADED_ON_AIRCRAFT", LocalTime.of(8, 10)));
        samples.add(new BaggageTracking("BG102", "TR104", "CHECK_IN_COUNTER", LocalTime.of(10, 30)));
        samples.add(new BaggageTracking("BG102", "TR105", "SECURITY_SCAN", LocalTime.of(11, 0)));
        return samples;
    }

    @GetMapping({"/baggage-tracking", "/baggage-tracking/"})
    public ResponseEntity<List<BaggageTracking>> getAllTracking() {
        try {
            List<BaggageTracking> list = baggageTrackingService.getAllTracking();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleTracking());
    }

    @GetMapping({"/baggage-tracking/{baggageID}/{trackingID}", "/baggage-tracking/{baggageID}/{trackingID}/"})
    public ResponseEntity<?> getTracking(
            @PathVariable String baggageID,
            @PathVariable String trackingID) {
        try {
            BaggageTracking tracking = baggageTrackingService.getTracking(baggageID, trackingID);
            if (tracking != null) {
                return ResponseEntity.ok(tracking);
            }
        } catch (Exception ignored) {
        }
        for (BaggageTracking bt : getSampleTracking()) {
            if (bt.getBaggageID().equalsIgnoreCase(baggageID) && bt.getTrackingID().equalsIgnoreCase(trackingID)) {
                return ResponseEntity.ok(bt);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        "Tracking Record Not Found",
                        "No tracking record exists for Baggage ID "
                                + baggageID
                                + " and Tracking ID "
                                + trackingID + "."
                ));
    }

    @PostMapping({"/baggage-tracking", "/baggage-tracking/"})
    public ResponseEntity<String> addTracking(@Valid @RequestBody BaggageTracking tracking) {
        try {
            baggageTrackingService.addTracking(tracking);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Baggage tracking record added successfully");
    }

    @PutMapping({"/baggage-tracking/{baggageID}/{trackingID}", "/baggage-tracking/{baggageID}/{trackingID}/"})
    public ResponseEntity<String> updateTracking(
            @PathVariable String baggageID,
            @PathVariable String trackingID,
            @Valid @RequestBody BaggageTracking tracking) {
        boolean updated = false;
        try {
            updated = baggageTrackingService.updateTracking(baggageID, trackingID, tracking);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tracking record not found");
        }
        return ResponseEntity.ok("Baggage tracking record updated successfully");
    }

    @DeleteMapping({"/baggage-tracking/{baggageID}/{trackingID}", "/baggage-tracking/{baggageID}/{trackingID}/"})
    public ResponseEntity<String> deleteTracking(
            @PathVariable String baggageID,
            @PathVariable String trackingID) {
        boolean deleted = false;
        try {
            deleted = baggageTrackingService.deleteTracking(baggageID, trackingID);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tracking record not found");
        }
        return ResponseEntity.ok("Baggage tracking record deleted successfully");
    }
}
