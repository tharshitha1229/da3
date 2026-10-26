package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Node("Flight")
public class Flight {

    @Id
    @NotBlank(message = "Flight ID is required")
    @Property("flightID")
    private String flightID;

    @NotBlank(message = "Airline ID is required")
    @Property("airlineID")
    private String airlineID;

    @NotNull(message = "Departure time is required")
    @Property("departureTime")
    private LocalTime departureTime;

    // Graph Relationships
    @Relationship(type = "DEPARTS_FROM", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"departingFlights", "arrivingFlights"})
    private Airport departureAirport;

    @Relationship(type = "ARRIVES_AT", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"departingFlights", "arrivingFlights"})
    private Airport arrivalAirport;

    @Relationship(type = "OPERATED_BY", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"flights"})
    private Airline airline;

    @Relationship(type = "FOR_FLIGHT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties({"flight"})
    private List<Reservation> reservations = new ArrayList<>();

    @Relationship(type = "LOADED_ON", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties({"flight"})
    private List<Baggage> baggageList = new ArrayList<>();

    public Flight() {
    }

    public Flight(String flightID, String airlineID, LocalTime departureTime) {
        this.flightID = flightID;
        this.airlineID = airlineID;
        this.departureTime = departureTime;
    }

    public Flight(String flightID, String airlineID, LocalTime departureTime,
                  Airport departureAirport, Airport arrivalAirport) {
        this.flightID = flightID;
        this.airlineID = airlineID;
        this.departureTime = departureTime;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getAirlineID() {
        return airlineID;
    }

    public void setAirlineID(String airlineID) {
        this.airlineID = airlineID;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Baggage> getBaggageList() {
        return baggageList;
    }

    public void setBaggageList(List<Baggage> baggageList) {
        this.baggageList = baggageList;
    }
}