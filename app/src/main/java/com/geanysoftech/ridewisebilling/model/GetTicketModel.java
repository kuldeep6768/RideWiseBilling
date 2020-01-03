package com.geanysoftech.ridewisebilling.model;

import java.io.Serializable;

public class GetTicketModel implements Serializable {

    private String ticketId;
    private String businessUnitName;
    private String pluCode;
    private String pluName;
    private String eventID;
    private String eventName;
    private String eventTypeName;
    private String resourceName;
    private String validFrom;
    private String validTo;

    public GetTicketModel(String ticketId, String businessUnitName, String pluCode, String pluName, String eventID,
                          String eventName, String eventTypeName, String resourceName, String validFrom, String validTo) {
        this.ticketId = ticketId;
        this.businessUnitName = businessUnitName;
        this.pluCode = pluCode;
        this.pluName = pluName;
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventTypeName = eventTypeName;
        this.resourceName = resourceName;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public String getPluCode() {
        return pluCode;
    }

    public void setPluCode(String pluCode) {
        this.pluCode = pluCode;
    }

    public String getPluName() {
        return pluName;
    }

    public void setPluName(String pluName) {
        this.pluName = pluName;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        return "GetTicketModel{" +
                "ticketId='" + ticketId + '\'' +
                ", businessUnitName='" + businessUnitName + '\'' +
                ", pluCode='" + pluCode + '\'' +
                ", pluName='" + pluName + '\'' +
                ", eventID='" + eventID + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validTo='" + validTo + '\'' +
                '}';
    }
}
