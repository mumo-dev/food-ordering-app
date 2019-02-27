package com.android.mumo.swahilicuisine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.interfaces.OnRestaurantSelectedListener;
import com.android.mumo.swahilicuisine.model.RestaurantApiData;
import com.android.mumo.swahilicuisine.services.RetrofitClient;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {


    private Context context;
    private List<RestaurantApiData> restaurantApiData;
    OnRestaurantSelectedListener listener;

    public static final String TAG = "RestaurantAdapter";

    public RestaurantAdapter(Context context, OnRestaurantSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        restaurantApiData = new ArrayList<>();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.restaurant_item_layout, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        holder.bind(restaurantApiData.get(position));
    }

    public void setRestaurantApiData(List<RestaurantApiData> restaurantApiData) {
        this.restaurantApiData = restaurantApiData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return restaurantApiData.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView mResName;
        private TextView mDelResTime;

        private RestaurantApiData resApi;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.rest_image);
            mResName = itemView.findViewById(R.id.rest_name);
            mDelResTime = itemView.findViewById(R.id.rest_time);
        }

        public void bind(RestaurantApiData data) {
            resApi = data;
            mResName.setText(data.getRestaurant().getName());
            mDelResTime.setText(data.getTime());
            Glide.with(context)
                    .load(RetrofitClient.BASE_URL + "images/" + data.getRestaurant().getImageUrl())
                    .into(imageView);

           // Log.i(TAG, "bind: "+ RetrofitClient.BASE_URL + "images/" + data.getRestaurant().getImageUrl());
        }

        @Override
        public void onClick(View v) {
            listener.onRestaurantSelected(resApi.getRestaurant().getId(), resApi.getRestaurant().getName(),
                    resApi.getRestaurant().getImageUrl(), resApi.getTime(), resApi.getFees());
        }
    }
}
