package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Payment")
public class Payment {

    @Id
    @NotBlank(message = "Payment ID is required")
    @Property("paymentID")
    private String paymentID;

    @NotBlank(message = "Reservation ID is required")
    @Property("reservationID")
    private String reservationID;

    @NotBlank(message = "Ticket number is required")
    @Property("ticketNo")
    private String ticketNo;

    @NotNull(message = "Amount is required")
    @Property("amount")
    private Double amount;

    @NotBlank(message = "Payment method is required")
    @Property("paymentMethod")
    private String paymentMethod;

    @Relationship(type = "PAID_FOR", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"tickets", "passenger", "flight"})
    private Reservation reservation;

    public Payment() {
    }

    public Payment(String paymentID, String reservationID,
                   String ticketNo, Double amount,
                   String paymentMethod) {
        this.paymentID = paymentID;
        this.reservationID = reservationID;
        this.ticketNo = ticketNo;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}