package com.android.mumo.swahilicuisine.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Restaurant  implements Serializable {
    private int id;
    private String name;

    @SerializedName("image_url")
    private String imageUrl;

    public Restaurant(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Restaurant() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
