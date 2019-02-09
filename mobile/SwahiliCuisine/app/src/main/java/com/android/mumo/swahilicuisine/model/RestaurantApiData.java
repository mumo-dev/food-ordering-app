package com.android.mumo.swahilicuisine.model;

import java.io.Serializable;

public class RestaurantApiData implements Serializable {

    private String time;
    private Double fees;
    private Restaurant restaurant;

    public RestaurantApiData(String time, Double fees, Restaurant restaurant) {
        this.time = time;
        this.fees = fees;
        this.restaurant = restaurant;
    }

    public RestaurantApiData() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
