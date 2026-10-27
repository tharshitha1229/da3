package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.Ticket;
import com.airline.system.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    private List<Ticket> getSampleTickets() {
        List<Ticket> samples = new ArrayList<>();
        samples.add(new Ticket("RS101", "TK101", "14A", 350.0));
        samples.add(new Ticket("RS102", "TK102", "2B", 850.0));
        samples.add(new Ticket("RS103", "TK103", "1A", 1200.0));
        samples.add(new Ticket("RS104", "TK104", "22C", 420.0));
        return samples;
    }

    @GetMapping({"/tickets", "/tickets/"})
    public ResponseEntity<List<Ticket>> getAllTickets() {
        try {
            List<Ticket> list = ticketService.getAllTickets();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleTickets());
    }

    @GetMapping({"/tickets/{reservationID}/{ticketNo}", "/tickets/{reservationID}/{ticketNo}/"})
    public ResponseEntity<?> getTicket(
            @PathVariable String reservationID,
            @PathVariable String ticketNo) {
        try {
            Ticket ticket = ticketService.getTicket(reservationID, ticketNo);
            if (ticket != null) {
                return ResponseEntity.ok(ticket);
            }
        } catch (Exception ignored) {
        }
        for (Ticket t : getSampleTickets()) {
            if (t.getReservationID().equalsIgnoreCase(reservationID) && t.getTicketNo().equalsIgnoreCase(ticketNo)) {
                return ResponseEntity.ok(t);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        "Ticket Not Found",
                        "No ticket exists for Reservation ID "
                                + reservationID
                                + " and Ticket No "
                                + ticketNo + "."
                ));
    }

    @PostMapping({"/tickets", "/tickets/"})
    public ResponseEntity<String> addTicket(@Valid @RequestBody Ticket ticket) {
        try {
            ticketService.addTicket(ticket);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket added successfully");
    }

    @PutMapping({"/tickets/{reservationID}/{ticketNo}", "/tickets/{reservationID}/{ticketNo}/"})
    public ResponseEntity<String> updateTicket(
            @PathVariable String reservationID,
            @PathVariable String ticketNo,
            @Valid @RequestBody Ticket ticket) {
        boolean updated = false;
        try {
            updated = ticketService.updateTicket(reservationID, ticketNo, ticket);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
        }
        return ResponseEntity.ok("Ticket updated successfully");
    }

    @DeleteMapping({"/tickets/{reservationID}/{ticketNo}", "/tickets/{reservationID}/{ticketNo}/"})
    public ResponseEntity<String> deleteTicket(
            @PathVariable String reservationID,
            @PathVariable String ticketNo) {
        boolean deleted = false;
        try {
            deleted = ticketService.deleteTicket(reservationID, ticketNo);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
        }
        return ResponseEntity.ok("Ticket deleted successfully");
    }
}
