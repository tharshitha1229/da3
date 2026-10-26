package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("PassengerPhone")
public class PassengerPhone {

    @NotBlank(message = "Passenger ID is required")
    @Property("passengerID")
    private String passengerID;

    @Id
    @NotBlank(message = "Phone is required")
    @Property("phone")
    private String phone;

    @Relationship(type = "HAS_PHONE", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties({"phones", "reservations", "baggage"})
    private Passenger passenger;

    public PassengerPhone() {
    }

    public PassengerPhone(String passengerID, String phone) {
        this.passengerID = passengerID;
        this.phone = phone;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}