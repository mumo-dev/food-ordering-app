package com.android.mumo.swahilicuisine.services;

import com.android.mumo.swahilicuisine.model.MenuApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MenuService {

    @GET("api/menus")
    Call<MenuApiResponse>fetchMenus();

}
