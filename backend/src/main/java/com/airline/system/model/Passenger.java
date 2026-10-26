package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Node("Passenger")
public class Passenger {

    @Id
    @NotBlank(message = "Passenger ID is required")
    @Property("passengerID")
    private String passengerID;

    @NotBlank(message = "Name is required")
    @Property("name")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Property("email")
    private String email;

    @NotNull(message = "Date of birth is required")
    @Property("dob")
    private LocalDate dob;

    @Property("street")
    private String street;

    @Property("city")
    private String city;

    @Property("pin")
    private String pin;

    // Graph Relationships
    @Relationship(type = "RESERVED", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"passenger"})
    private List<Reservation> reservations = new ArrayList<>();

    @Relationship(type = "OWNS_BAGGAGE", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"passenger"})
    private List<Baggage> baggage = new ArrayList<>();

    @Relationship(type = "HAS_PHONE", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties({"passenger"})
    private List<PassengerPhone> phones = new ArrayList<>();

    public Passenger() {
    }

    public Passenger(String passengerID, String name, String email,
                     LocalDate dob, String street, String city, String pin) {
        this.passengerID = passengerID;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.street = street;
        this.city = city;
        this.pin = pin;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Baggage> getBaggage() {
        return baggage;
    }

    public void setBaggage(List<Baggage> baggage) {
        this.baggage = baggage;
    }

    public List<PassengerPhone> getPhones() {
        return phones;
    }

    public void setPhones(List<PassengerPhone> phones) {
        this.phones = phones;
    }
}