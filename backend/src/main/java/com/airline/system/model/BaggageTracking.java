package com.airline.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalTime;

@Node("BaggageTracking")
public class BaggageTracking {

    @NotBlank(message = "Baggage ID is required")
    @Property("baggageID")
    private String baggageID;

    @Id
    @NotBlank(message = "Tracking ID is required")
    @Property("trackingID")
    private String trackingID;

    @NotBlank(message = "Scan location is required")
    @Property("scanLocation")
    private String scanLocation;

    @NotNull(message = "Scan time is required")
    @Property("scanTime")
    private LocalTime scanTime;

    @Relationship(type = "TRACKED_AT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties({"trackingHistory", "passenger", "flight", "reservation"})
    private Baggage baggage;

    public BaggageTracking() {
    }

    public BaggageTracking(String baggageID, String trackingID,
                           String scanLocation, LocalTime scanTime) {
        this.baggageID = baggageID;
        this.trackingID = trackingID;
        this.scanLocation = scanLocation;
        this.scanTime = scanTime;
    }

    public String getBaggageID() {
        return baggageID;
    }

    public void setBaggageID(String baggageID) {
        this.baggageID = baggageID;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public String getScanLocation() {
        return scanLocation;
    }

    public void setScanLocation(String scanLocation) {
        this.scanLocation = scanLocation;
    }

    public LocalTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(LocalTime scanTime) {
        this.scanTime = scanTime;
    }

    public Baggage getBaggage() {
        return baggage;
    }

    public void setBaggage(Baggage baggage) {
        this.baggage = baggage;
    }
}