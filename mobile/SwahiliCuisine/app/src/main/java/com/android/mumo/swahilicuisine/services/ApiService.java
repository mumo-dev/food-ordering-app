package com.android.mumo.swahilicuisine.services;

import com.android.mumo.swahilicuisine.model.Area;
import com.android.mumo.swahilicuisine.model.Blog;
import com.android.mumo.swahilicuisine.model.Menu;
import com.android.mumo.swahilicuisine.model.Orderjst;
import com.android.mumo.swahilicuisine.model.RealOrder;
import com.android.mumo.swahilicuisine.model.RestaurantApiData;
import com.android.mumo.swahilicuisine.model.Town;
import com.android.mumo.swahilicuisine.model.User;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/towns")
    Call<List<Town>> fetchTowns();

    @GET("api/towns/areas/{id}")
    Call<List<Area>> fetchArea(@Path("id") int id);

    @GET("api/area/{id}/restaurants")
    Call<List<RestaurantApiData>> fetchRestaurants(@Path("id") int id);

    @GET("api/restaurants/{restId}/menus")
    Call<List<Menu>> fetchMenu(@Path("restId") int restId);

    @POST("api/users/register")
    Call<User> registerUser(@Body User user);

    @POST("api/users/login")
    Call<User> logInUser(@Body User user);

    @POST("api/orders")
    Call<JSONObject> storeOrder(@Header("Authorization") String authorization, @Body Orderjst data);

    @GET("api/orders/users/{id}")
    Call<List<RealOrder>> getOrders(@Header("Authorization") String authorization, @Path("id") int userId);

    @GET("api/blog")
    Call<List<Blog>> fetchBlogPosts();
}
