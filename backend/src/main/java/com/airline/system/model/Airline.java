package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Airline")
public class Airline {

    @Id
    @NotBlank(message = "Airline ID is required")
    @Property("airlineID")
    private String airlineID;

    @NotBlank(message = "Airline name is required")
    @Property("airlineName")
    private String airlineName;

    @NotBlank(message = "IATA code is required")
    @Property("iataCode")
    private String iataCode;

    @Relationship(type = "OPERATED_BY", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties({"airline"})
    private List<Flight> flights = new ArrayList<>();

    public Airline() {
    }

    public Airline(String airlineID, String airlineName, String iataCode) {
        this.airlineID = airlineID;
        this.airlineName = airlineName;
        this.iataCode = iataCode;
    }

    public String getAirlineID() {
        return airlineID;
    }

    public void setAirlineID(String airlineID) {
        this.airlineID = airlineID;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}