package com.geanysoftech.ridewisebilling.model;

import java.io.Serializable;

public class PluTaxesModel implements Serializable {

    private String taxID;
    private String taxDescription;
    private String taxPercent;

    public PluTaxesModel() {
    }

    public PluTaxesModel(String taxID, String taxDescription, String taxPercent) {
        this.taxID = taxID;
        this.taxDescription = taxDescription;
        this.taxPercent = taxPercent;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getTaxDescription() {
        return taxDescription;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    public String getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        this.taxPercent = taxPercent;
    }

    @Override
    public String toString() {
        return "PluTaxesModel{" +
                "taxID='" + taxID + '\'' +
                ", taxDescription='" + taxDescription + '\'' +
                ", taxPercent='" + taxPercent + '\'' +
                '}';
    }
}
