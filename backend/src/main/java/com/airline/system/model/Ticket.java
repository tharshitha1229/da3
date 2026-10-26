package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Ticket")
public class Ticket {

    @NotBlank(message = "Reservation ID is required")
    @Property("reservationID")
    private String reservationID;

    @Id
    @NotBlank(message = "Ticket number is required")
    @Property("ticketNo")
    private String ticketNo;

    @Property("seatNo")
    private String seatNo;

    @NotNull(message = "Fare is required")
    @Property("fare")
    private Double fare;

    @Relationship(type = "HAS_TICKET", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties({"tickets", "flight", "passenger"})
    private Reservation reservation;

    @Relationship(type = "HAS_BAGGAGE", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"reservation", "flight", "passenger"})
    private List<Baggage> baggageList = new ArrayList<>();

    public Ticket() {
    }

    public Ticket(String reservationID, String ticketNo,
                  String seatNo, Double fare) {
        this.reservationID = reservationID;
        this.ticketNo = ticketNo;
        this.seatNo = seatNo;
        this.fare = fare;
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

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<Baggage> getBaggageList() {
        return baggageList;
    }

    public void setBaggageList(List<Baggage> baggageList) {
        this.baggageList = baggageList;
    }
}