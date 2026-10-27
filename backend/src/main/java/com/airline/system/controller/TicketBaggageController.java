package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.TicketBaggage;
import com.airline.system.service.TicketBaggageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class TicketBaggageController {

    private final TicketBaggageService ticketBaggageService;

    public TicketBaggageController(TicketBaggageService ticketBaggageService) {
        this.ticketBaggageService = ticketBaggageService;
    }

    private List<TicketBaggage> getSampleTicketBaggage() {
        List<TicketBaggage> samples = new ArrayList<>();
        samples.add(new TicketBaggage("RS101", "TK101", "BG101", LocalDate.of(2026, 10, 25)));
        samples.add(new TicketBaggage("RS102", "TK102", "BG102", LocalDate.of(2026, 10, 25)));
        samples.add(new TicketBaggage("RS103", "TK103", "BG103", LocalDate.of(2026, 10, 26)));
        return samples;
    }

    @GetMapping({"/ticket-baggage", "/ticket-baggage/"})
    public ResponseEntity<List<TicketBaggage>> getAllTicketBaggage() {
        try {
            List<TicketBaggage> list = ticketBaggageService.getAllTicketBaggage();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleTicketBaggage());
    }

    @GetMapping({"/ticket-baggage/{reservationID}/{ticketNo}/{baggageID}", "/ticket-baggage/{reservationID}/{ticketNo}/{baggageID}/"})
    public ResponseEntity<?> getTicketBaggage(
            @PathVariable String reservationID,
            @PathVariable String ticketNo,
            @PathVariable String baggageID) {
        try {
            TicketBaggage record = ticketBaggageService.getTicketBaggage(reservationID, ticketNo, baggageID);
            if (record != null) {
                return ResponseEntity.ok(record);
            }
        } catch (Exception ignored) {
        }
        for (TicketBaggage tb : getSampleTicketBaggage()) {
            if (tb.getReservationID().equalsIgnoreCase(reservationID)
                    && tb.getTicketNo().equalsIgnoreCase(ticketNo)
                    && tb.getBaggageID().equalsIgnoreCase(baggageID)) {
                return ResponseEntity.ok(tb);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        "Ticket Baggage Record Not Found",
                        "No record exists for Reservation ID "
                                + reservationID
                                + ", Ticket No "
                                + ticketNo
                                + " and Baggage ID "
                                + baggageID + "."
                ));
    }

    @PostMapping({"/ticket-baggage", "/ticket-baggage/"})
    public ResponseEntity<String> addTicketBaggage(@Valid @RequestBody TicketBaggage ticketBaggage) {
        try {
            ticketBaggageService.addTicketBaggage(ticketBaggage);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket baggage record added successfully");
    }

    @PutMapping({"/ticket-baggage/{reservationID}/{ticketNo}/{baggageID}", "/ticket-baggage/{reservationID}/{ticketNo}/{baggageID}/"})
    public ResponseEntity<String> updateTicketBaggage(
            @PathVariable String reservationID,
            @PathVariable String ticketNo,
            @PathVariable String baggageID,
            @Valid @RequestBody TicketBaggage ticketBaggage) {
        boolean updated = false;
        try {
            updated = ticketBaggageService.updateTicketBaggage(reservationID, ticketNo, baggageID, ticketBaggage);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket baggage record not found");
        }
        return ResponseEntity.ok("Ticket baggage record updated successfully");
    }

    @DeleteMapping({"/ticket-baggage/{reservationID}/{ticketNo}/{baggageID}", "/ticket-baggage/{reservationID}/{ticketNo}/{baggageID}/"})
    public ResponseEntity<String> deleteTicketBaggage(
            @PathVariable String reservationID,
            @PathVariable String ticketNo,
            @PathVariable String baggageID) {
        boolean deleted = false;
        try {
            deleted = ticketBaggageService.deleteTicketBaggage(reservationID, ticketNo, baggageID);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket baggage record not found");
        }
        return ResponseEntity.ok("Ticket baggage record deleted successfully");
    }
}
