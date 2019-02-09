package com.android.mumo.swahilicuisine.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.adapters.RestaurantAdapter;
import com.android.mumo.swahilicuisine.interfaces.OnRestaurantClickListener;
import com.android.mumo.swahilicuisine.interfaces.OnRestaurantSelectedListener;
import com.android.mumo.swahilicuisine.model.RestaurantApiData;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;
import com.android.mumo.swahilicuisine.utils.PreferenceUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsFragment extends Fragment implements OnRestaurantSelectedListener {

    private RecyclerView recyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView errorTv;
    private LinearLayout linearLayout;


    private RestaurantAdapter restaurantAdapter;
    private int areaId;

    OnRestaurantClickListener mListener;

    public static RestaurantsFragment newInstance() {
        RestaurantsFragment fragment = new RestaurantsFragment();
        return fragment;
    }

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnRestaurantClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantAdapter = new RestaurantAdapter(getActivity(), this);
        areaId = PreferenceUtils.getLocationAreaId(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.restuarants_recycler_view);
        mLoadingIndicator = view.findViewById(R.id.pg_loading_indicator);
        errorTv = view.findViewById(R.id.tv_error);
        linearLayout = view.findViewById(R.id.view_empty_restaurants);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(restaurantAdapter);

        fetchData();

        errorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });
    }

    private void uiLoading() {
        recyclerView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        errorTv.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

    }

    private void uiLoaded() {
        recyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
    }

    private void uiError(String message) {
        recyclerView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        if (message != null) {
            errorTv.setText(message + "\n Click to retry");
        }
    }


    private void fetchData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        uiLoading();
        apiService.fetchRestaurants(areaId).enqueue(new Callback<List<RestaurantApiData>>() {
            @Override
            public void onResponse(Call<List<RestaurantApiData>> call, Response<List<RestaurantApiData>> response) {
                uiLoaded();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        restaurantAdapter.setRestaurantApiData(response.body());
                    }
                    if (response.body() == null || response.body().size() == 0) {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RestaurantApiData>> call, Throwable t) {
                uiError(t.getMessage());
            }
        });
    }


    @Override
    public void onRestaurantSelected(int resId, String resName, String url, String time, Double deliveryFee) {
        mListener.onRestaurantClick(resId, resName, url, time, deliveryFee);
    }
}
