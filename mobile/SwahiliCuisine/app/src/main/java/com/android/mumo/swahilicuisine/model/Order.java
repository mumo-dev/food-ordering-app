package com.android.mumo.swahilicuisine.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private int id;
    private String restaurantName;


    private double deliveryCost;
    private String deliveryTime;
    private List<OrderItem> items;

    public Order() {
    }

    public Order(double deliveryCost, String deliveryTime, List<OrderItem> items) {
        this.deliveryCost = deliveryCost;
        this.deliveryTime = deliveryTime;
        this.items = items;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String status) {
        this.restaurantName = status;
    }
}
