package com.android.mumo.swahilicuisine.model;

import java.io.Serializable;

public class RecipeBook implements Serializable {

    private int id;
    private String title;
    private String description;
    private String updatedAt;
    private String downloadUrl;


    public RecipeBook() {
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String date) {
        this.updatedAt = date;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
