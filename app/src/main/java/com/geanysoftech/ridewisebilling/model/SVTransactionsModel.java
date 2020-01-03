package com.geanysoftech.ridewisebilling.model;

import java.io.Serializable;

public class SVTransactionsModel implements Serializable {

    private String svId;
    private String transactionDate;
    private String sourceName;
    private String externalID;
    private String amount;
    private String id;
    private String transactionType;
    private String nodeNo;

    public SVTransactionsModel(String svId, String transactionDate, String sourceName,
                               String externalID, String amount, String id, String transactionType, String nodeNo) {
        this.svId = svId;
        this.transactionDate = transactionDate;
        this.sourceName = sourceName;
        this.externalID = externalID;
        this.amount = amount;
        this.id = id;
        this.transactionType = transactionType;
        this.nodeNo = nodeNo;
    }

    public String getSvId() {
        return svId;
    }

    public void setSvId(String svId) {
        this.svId = svId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getNodeNo() {
        return nodeNo;
    }

    public void setNodeNo(String nodeNo) {
        this.nodeNo = nodeNo;
    }

    @Override
    public String toString() {
        return "SVTransactionsModel{" +
                "svId='" + svId + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", externalID='" + externalID + '\'' +
                ", amount='" + amount + '\'' +
                ", id='" + id + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", nodeNo='" + nodeNo + '\'' +
                '}';
    }
}
