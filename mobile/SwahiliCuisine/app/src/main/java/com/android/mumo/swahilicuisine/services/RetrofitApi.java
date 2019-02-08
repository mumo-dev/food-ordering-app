package com.android.mumo.swahilicuisine.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    private static RetrofitApi sINSTANCE;
    private Retrofit retrofit;


    private  RetrofitApi(){
         retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.3.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static RetrofitApi getInstance(){
        synchronized (RetrofitApi.class){
            if(sINSTANCE == null){
                sINSTANCE = new RetrofitApi();
            }
            return sINSTANCE;
        }
    }

    public MenuService create(){
        return retrofit.create(MenuService.class);
    }
}
