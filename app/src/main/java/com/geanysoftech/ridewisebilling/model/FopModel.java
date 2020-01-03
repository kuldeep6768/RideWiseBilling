package com.geanysoftech.ridewisebilling.model;

import java.io.Serializable;

public class FopModel implements Serializable {

    private String fopId;
    private String fopType;

    public FopModel(String fopId, String fopType) {
        this.fopId = fopId;
        this.fopType = fopType;
    }

    public String getFopId() {
        return fopId;
    }

    public void setFopId(String fopId) {
        this.fopId = fopId;
    }

    public String getFopType() {
        return fopType;
    }

    public void setFopType(String fopType) {
        this.fopType = fopType;
    }

    @Override
    public String toString() {
        return "FopModel{" +
                "fopId='" + fopId + '\'' +
                ", fopType='" + fopType + '\'' +
                '}';
    }
}
