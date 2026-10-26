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

@Node("Baggage")
public class Baggage {

    @Id
    @NotBlank(message = "Baggage ID is required")
    @Property("baggageID")
    private String baggageID;

    @NotNull(message = "Number of pieces is required")
    @Property("noOfPieces")
    private Integer noOfPieces;

    @NotNull(message = "Weight is required")
    @Property("weight")
    private Double weight;

    // Graph Relationships
    @Relationship(type = "OWNS_BAGGAGE", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties({"baggage", "reservations", "phones"})
    private Passenger passenger;

    @Relationship(type = "LOADED_ON", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"baggageList", "reservations"})
    private Flight flight;

    @Relationship(type = "CHECKED_FOR", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"tickets", "flight", "passenger"})
    private Reservation reservation;

    @Relationship(type = "TRACKED_AT", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"baggage"})
    private List<BaggageTracking> trackingHistory = new ArrayList<>();

    public Baggage() {
    }

    public Baggage(String baggageID, Integer noOfPieces, Double weight) {
        this.baggageID = baggageID;
        this.noOfPieces = noOfPieces;
        this.weight = weight;
    }

    public Baggage(String baggageID, Integer noOfPieces, Double weight,
                   Passenger passenger, Flight flight) {
        this.baggageID = baggageID;
        this.noOfPieces = noOfPieces;
        this.weight = weight;
        this.passenger = passenger;
        this.flight = flight;
    }

    public String getBaggageID() {
        return baggageID;
    }

    public void setBaggageID(String baggageID) {
        this.baggageID = baggageID;
    }

    public Integer getNoOfPieces() {
        return noOfPieces;
    }

    public void setNoOfPieces(Integer noOfPieces) {
        this.noOfPieces = noOfPieces;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<BaggageTracking> getTrackingHistory() {
        return trackingHistory;
    }

    public void setTrackingHistory(List<BaggageTracking> trackingHistory) {
        this.trackingHistory = trackingHistory;
    }
}