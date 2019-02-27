package com.android.mumo.swahilicuisine.model;

import java.util.List;

public class Orderjst {

    private int userId;
    private int areaId;
    private double cost;
    private String deliveryTime;
    private List<Menujst> items;

    public Orderjst() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Menujst> getItems() {
        return items;
    }

    public void setItems(List<Menujst> items) {
        this.items = items;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
