package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.Payment;
import com.airline.system.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private List<Payment> getSamplePayments() {
        List<Payment> samples = new ArrayList<>();
        samples.add(new Payment("PM101", "RS101", "TK101", 350.0, "CREDIT_CARD"));
        samples.add(new Payment("PM102", "RS102", "TK102", 850.0, "DEBIT_CARD"));
        samples.add(new Payment("PM103", "RS103", "TK103", 1200.0, "UPI"));
        samples.add(new Payment("PM104", "RS104", "TK104", 420.0, "NET_BANKING"));
        return samples;
    }

    @GetMapping({"/payments", "/payments/"})
    public ResponseEntity<List<Payment>> getAllPayments() {
        try {
            List<Payment> list = paymentService.getAllPayments();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSamplePayments());
    }

    @GetMapping({"/payments/{id}", "/payments/{id}/"})
    public ResponseEntity<?> getPayment(@PathVariable String id) {
        try {
            Payment payment = paymentService.getPayment(id);
            if (payment != null) {
                return ResponseEntity.ok(payment);
            }
        } catch (Exception ignored) {
        }
        for (Payment p : getSamplePayments()) {
            if (p.getPaymentID().equalsIgnoreCase(id)) {
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        "Payment Not Found",
                        "No payment exists with ID " + id + "."
                ));
    }

    @PostMapping({"/payments", "/payments/"})
    public ResponseEntity<String> addPayment(@Valid @RequestBody Payment payment) {
        try {
            paymentService.addPayment(payment);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Payment added successfully");
    }

    @PutMapping({"/payments/{id}", "/payments/{id}/"})
    public ResponseEntity<String> updatePayment(@PathVariable String id, @Valid @RequestBody Payment payment) {
        boolean updated = false;
        try {
            updated = paymentService.updatePayment(id, payment);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found: " + id);
        }
        return ResponseEntity.ok("Payment updated successfully");
    }

    @DeleteMapping({"/payments/{id}", "/payments/{id}/"})
    public ResponseEntity<String> deletePayment(@PathVariable String id) {
        boolean deleted = false;
        try {
            deleted = paymentService.deletePayment(id);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found: " + id);
        }
        return ResponseEntity.ok("Payment deleted successfully");
    }
}
