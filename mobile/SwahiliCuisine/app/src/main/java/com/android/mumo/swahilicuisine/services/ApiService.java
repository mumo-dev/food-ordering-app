package com.android.mumo.swahilicuisine.services;

import com.android.mumo.swahilicuisine.model.Area;
import com.android.mumo.swahilicuisine.model.Menu;
import com.android.mumo.swahilicuisine.model.RestaurantApiData;
import com.android.mumo.swahilicuisine.model.Town;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/towns")
    Call<List<Town>> fetchTowns();

    @GET("api/towns/areas/{id}")
    Call<List<Area>> fetchArea(@Path("id") int id);

    @GET("api/area/{id}/restaurants")
    Call<List<RestaurantApiData>> fetchRestaurants(@Path("id") int id);

    @GET("/api/restaurants/{restId}/menus")
    Call<List<Menu>> fetchMenu(@Path("restId") int restId);
}
