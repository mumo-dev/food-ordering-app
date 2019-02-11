package com.android.mumo.swahilicuisine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.mumo.swahilicuisine.model.Order;

public class OrderViewModel extends ViewModel {

    private MutableLiveData<Order> orderLiveData;


    public LiveData<Order> getOrderLiveData() {
        if (orderLiveData == null) {
            orderLiveData = new MutableLiveData<>();
        }
        return orderLiveData;
    }

    public void setOrderLiveData(Order order) {
        if (orderLiveData == null) {
            orderLiveData = new MutableLiveData<>();
        }
        orderLiveData.setValue(order);
    }
}
