package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Airport")
public class Airport {

    @Id
    @NotBlank(message = "Airport ID is required")
    @Property("airportID")
    private String airportID;

    @NotBlank(message = "Airport name is required")
    @Property("airportName")
    private String airportName;

    @NotBlank(message = "City is required")
    @Property("city")
    private String city;

    @NotBlank(message = "Country is required")
    @Property("country")
    private String country;

    // Incoming graph relationships
    @JsonIgnore
    @Relationship(type = "DEPARTS_FROM", direction = Relationship.Direction.INCOMING)
    private List<Flight> departingFlights = new ArrayList<>();

    @JsonIgnore
    @Relationship(type = "ARRIVES_AT", direction = Relationship.Direction.INCOMING)
    private List<Flight> arrivingFlights = new ArrayList<>();

    public Airport() {
    }

    public Airport(String airportID, String airportName,
                   String city, String country) {
        this.airportID = airportID;
        this.airportName = airportName;
        this.city = city;
        this.country = country;
    }

    public String getAirportID() {
        return airportID;
    }

    public void setAirportID(String airportID) {
        this.airportID = airportID;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Flight> getDepartingFlights() {
        return departingFlights;
    }

    public void setDepartingFlights(List<Flight> departingFlights) {
        this.departingFlights = departingFlights;
    }

    public List<Flight> getArrivingFlights() {
        return arrivingFlights;
    }

    public void setArrivingFlights(List<Flight> arrivingFlights) {
        this.arrivingFlights = arrivingFlights;
    }
}