package com.geanysoftech.ridewisebilling.model;

import java.io.Serializable;
import java.util.List;

public class SelectedItemModel implements Serializable {

    private String id;
    private String pluCode;
    private String pluName;
    private String pluType;
    private String hsn;
    private String price;
    private String deposit;
    private String eventTypeID;
    private String resourceID;
    private String eventID;
    private String quantity;
    private String discountPercent;
    private String discountAmount;
    private List<PluTaxesModel> taxes;
    private List<PluChildItemsModel> childItems;
    private String totalAmount;

    private String amount;

    public SelectedItemModel() {
    }

    public SelectedItemModel(String pluCode, String pluName, String quantity, String price, String amount) {
        this.pluCode = pluCode;
        this.pluName = pluName;
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;
    }

    public SelectedItemModel(String id, String pluCode, String pluName, String pluType, String hsn, String price, String deposit,
                             String eventTypeID, String resourceID, String eventID, String quantity, String discountPercent,
                             String discountAmount, List<PluTaxesModel> taxes, List<PluChildItemsModel> childItems, String amount, String totalAmount) {
        this.id = id;
        this.pluCode = pluCode;
        this.pluName = pluName;
        this.pluType = pluType;
        this.hsn = hsn;
        this.price = price;
        this.deposit = deposit;
        this.eventTypeID = eventTypeID;
        this.resourceID = resourceID;
        this.eventID = eventID;
        this.quantity = quantity;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.taxes = taxes;
        this.childItems = childItems;
        this.amount = amount;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPluType() {
        return pluType;
    }

    public void setPluType(String pluType) {
        this.pluType = pluType;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getEventTypeID() {
        return eventTypeID;
    }

    public void setEventTypeID(String eventTypeID) {
        this.eventTypeID = eventTypeID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public List<PluTaxesModel> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<PluTaxesModel> taxes) {
        this.taxes = taxes;
    }

    public List<PluChildItemsModel> getChildItems() {
        return childItems;
    }

    public void setChildItems(List<PluChildItemsModel> childItems) {
        this.childItems = childItems;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "SelectedItemModel{" +
                "id='" + id + '\'' +
                ", pluCode='" + pluCode + '\'' +
                ", pluName='" + pluName + '\'' +
                ", pluType='" + pluType + '\'' +
                ", hsn='" + hsn + '\'' +
                ", price='" + price + '\'' +
                ", deposit='" + deposit + '\'' +
                ", eventTypeID='" + eventTypeID + '\'' +
                ", resourceID='" + resourceID + '\'' +
                ", eventID='" + eventID + '\'' +
                ", quantity='" + quantity + '\'' +
                ", discountPercent='" + discountPercent + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", taxes=" + taxes +
                ", childItems=" + childItems +
                ", totalAmount='" + totalAmount + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
