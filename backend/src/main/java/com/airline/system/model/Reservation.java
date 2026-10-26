package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Reservation")
public class Reservation {

    @Id
    @NotBlank(message = "Reservation ID is required")
    @Property("reservationID")
    private String reservationID;

    @NotBlank(message = "Passenger ID is required")
    @Property("passengerID")
    private String passengerID;

    @NotBlank(message = "Booking status is required")
    @Property("bookingStatus")
    private String bookingStatus;

    @NotBlank(message = "Class is required")
    @Property("reservationClass")
    private String reservationClass;

    @NotBlank(message = "Departure airport ID is required")
    @Property("departureAirportID")
    private String departureAirportID;

    @NotBlank(message = "Arrival airport ID is required")
    @Property("arrivalAirportID")
    private String arrivalAirportID;

    // Graph Relationships
    @Relationship(type = "RESERVED", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties({"reservations", "baggage", "phones"})
    private Passenger passenger;

    @Relationship(type = "DEPARTS_FROM", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"departingFlights", "arrivingFlights"})
    private Airport departureAirport;

    @Relationship(type = "ARRIVES_AT", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"departingFlights", "arrivingFlights"})
    private Airport arrivalAirport;

    @Relationship(type = "FOR_FLIGHT", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"reservations", "baggageList"})
    private Flight flight;

    @Relationship(type = "HAS_TICKET", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"reservation"})
    private List<Ticket> tickets = new ArrayList<>();

    public Reservation() {
    }

    public Reservation(String reservationID, String passengerID,
                       String bookingStatus, String reservationClass,
                       String departureAirportID, String arrivalAirportID) {
        this.reservationID = reservationID;
        this.passengerID = passengerID;
        this.bookingStatus = bookingStatus;
        this.reservationClass = reservationClass;
        this.departureAirportID = departureAirportID;
        this.arrivalAirportID = arrivalAirportID;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getReservationClass() {
        return reservationClass;
    }

    public void setReservationClass(String reservationClass) {
        this.reservationClass = reservationClass;
    }

    public String getDepartureAirportID() {
        return departureAirportID;
    }

    public void setDepartureAirportID(String departureAirportID) {
        this.departureAirportID = departureAirportID;
    }

    public String getArrivalAirportID() {
        return arrivalAirportID;
    }

    public void setArrivalAirportID(String arrivalAirportID) {
        this.arrivalAirportID = arrivalAirportID;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
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

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}