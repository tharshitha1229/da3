package com.airline.system.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;

@Node("TicketBaggage")
public class TicketBaggage {

    @Id
    private String id;

    @NotBlank(message = "Reservation ID is required")
    @Property("reservationID")
    private String reservationID;

    @NotBlank(message = "Ticket number is required")
    @Property("ticketNo")
    private String ticketNo;

    @NotBlank(message = "Baggage ID is required")
    @Property("baggageID")
    private String baggageID;

    @NotNull(message = "Checked-in date is required")
    @Property("checkedInDate")
    private LocalDate checkedInDate;

    public TicketBaggage() {
    }

    public TicketBaggage(String reservationID, String ticketNo,
                         String baggageID, LocalDate checkedInDate) {
        this.id = buildCompositeId(reservationID, ticketNo, baggageID);
        this.reservationID = reservationID;
        this.ticketNo = ticketNo;
        this.baggageID = baggageID;
        this.checkedInDate = checkedInDate;
    }

    public static String buildCompositeId(String reservationID, String ticketNo, String baggageID) {
        return reservationID + "_" + ticketNo + "_" + baggageID;
    }

    public String getId() {
        if (id == null && reservationID != null && ticketNo != null && baggageID != null) {
            id = buildCompositeId(reservationID, ticketNo, baggageID);
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
        if (this.ticketNo != null && this.baggageID != null) {
            this.id = buildCompositeId(reservationID, this.ticketNo, this.baggageID);
        }
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
        if (this.reservationID != null && this.baggageID != null) {
            this.id = buildCompositeId(this.reservationID, ticketNo, this.baggageID);
        }
    }

    public String getBaggageID() {
        return baggageID;
    }

    public void setBaggageID(String baggageID) {
        this.baggageID = baggageID;
        if (this.reservationID != null && this.ticketNo != null) {
            this.id = buildCompositeId(this.reservationID, this.ticketNo, baggageID);
        }
    }

    public LocalDate getCheckedInDate() {
        return checkedInDate;
    }

    public void setCheckedInDate(LocalDate checkedInDate) {
        this.checkedInDate = checkedInDate;
    }
}