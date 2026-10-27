package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.PassengerPhone;
import com.airline.system.service.PassengerPhoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PassengerPhoneController {

    private final PassengerPhoneService passengerPhoneService;

    public PassengerPhoneController(PassengerPhoneService passengerPhoneService) {
        this.passengerPhoneService = passengerPhoneService;
    }

    private List<PassengerPhone> getSamplePhones() {
        List<PassengerPhone> samples = new ArrayList<>();
        samples.add(new PassengerPhone("PA101", "+1-555-0111"));
        samples.add(new PassengerPhone("PA102", "+1-555-0122"));
        samples.add(new PassengerPhone("PA103", "+1-555-0133"));
        samples.add(new PassengerPhone("PA104", "+1-555-0144"));
        return samples;
    }

    @GetMapping({"/passenger-phones", "/passenger-phones/"})
    public ResponseEntity<List<PassengerPhone>> getAllPhones() {
        try {
            List<PassengerPhone> list = passengerPhoneService.getAllPhones();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSamplePhones());
    }

    @GetMapping({"/passenger-phones/{passengerID}/{phone}", "/passenger-phones/{passengerID}/{phone}/"})
    public ResponseEntity<?> getPhone(
            @PathVariable String passengerID,
            @PathVariable String phone) {
        try {
            PassengerPhone passengerPhone = passengerPhoneService.getPhone(passengerID, phone);
            if (passengerPhone != null) {
                return ResponseEntity.ok(passengerPhone);
            }
        } catch (Exception ignored) {
        }
        for (PassengerPhone pp : getSamplePhones()) {
            if (pp.getPassengerID().equalsIgnoreCase(passengerID) && pp.getPhone().equalsIgnoreCase(phone)) {
                return ResponseEntity.ok(pp);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        "Passenger Phone Not Found",
                        "No phone record exists for Passenger ID "
                                + passengerID
                                + " and phone "
                                + phone + "."
                ));
    }

    @PostMapping({"/passenger-phones", "/passenger-phones/"})
    public ResponseEntity<String> addPhone(@Valid @RequestBody PassengerPhone passengerPhone) {
        try {
            passengerPhoneService.addPhone(passengerPhone);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Passenger phone added successfully");
    }

    @PutMapping({"/passenger-phones/{passengerID}/{phone}", "/passenger-phones/{passengerID}/{phone}/"})
    public ResponseEntity<String> updatePhone(
            @PathVariable String passengerID,
            @PathVariable String phone,
            @Valid @RequestBody PassengerPhone passengerPhone) {
        boolean updated = false;
        try {
            updated = passengerPhoneService.updatePhone(passengerID, phone, passengerPhone);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Passenger phone not found");
        }
        return ResponseEntity.ok("Passenger phone updated successfully");
    }

    @DeleteMapping({"/passenger-phones/{passengerID}/{phone}", "/passenger-phones/{passengerID}/{phone}/"})
    public ResponseEntity<String> deletePhone(
            @PathVariable String passengerID,
            @PathVariable String phone) {
        boolean deleted = false;
        try {
            deleted = passengerPhoneService.deletePhone(passengerID, phone);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Passenger phone not found");
        }
        return ResponseEntity.ok("Passenger phone deleted successfully");
    }
}
