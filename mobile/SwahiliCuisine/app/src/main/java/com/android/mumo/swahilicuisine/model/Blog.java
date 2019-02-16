package com.android.mumo.swahilicuisine.model;

import java.io.Serializable;

public class Blog implements Serializable {

    private int id;
    private String title;
    private String description;
    private String createdAt;


    public Blog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String date) {
        this.createdAt = date;
    }
}
