package com.android.mumo.swahilicuisine.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.LoginActivity;
import com.android.mumo.swahilicuisine.OrderDetailActivity;
import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.adapters.RealOrderAdapter;
import com.android.mumo.swahilicuisine.model.Order;
import com.android.mumo.swahilicuisine.model.RealOrder;
import com.android.mumo.swahilicuisine.model.User;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;
import com.android.mumo.swahilicuisine.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment implements RealOrderAdapter.OnOrderItemClicked {

    private int userId;
    private String token;

    private RecyclerView recyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView errorTv;
    private LinearLayout linearLayout;

    private RealOrderAdapter adapter;

    public static final String TAG = "OrderFragment";

    private List<RealOrder> orders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = PreferenceUtils.getUserDetails(getActivity());
        if (user == null) {
            openLoginActivity();
        }

        userId = user.getId();
        token = user.getToken();

        orders = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.order_recylerView);
        mLoadingIndicator = view.findViewById(R.id.pg_loading_indicator);
        errorTv = view.findViewById(R.id.tv_error);
        linearLayout = view.findViewById(R.id.view_empty_menu);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RealOrderAdapter(getActivity(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fetchOrders();

        errorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchOrders();
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


    private void fetchOrders() {
        if (userId != 0 && token != null) {
            ApiService service = RetrofitClient.getClient().create(ApiService.class);
            uiLoading();
            service.getOrders("JWT " + token, userId).enqueue(new Callback<List<RealOrder>>() {
                @Override
                public void onResponse(Call<List<RealOrder>> call, Response<List<RealOrder>> response) {
                    uiLoaded();
                    if (response.isSuccessful()) {
                        if(response.body() != null) {
                            orders = response.body();
                            adapter.setOrderList(response.body());
                        }
                        if (response.body() == null || response.body().size() == 0) {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<RealOrder>> call, Throwable t) {
                    uiError(t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "You need to be logged in", Toast.LENGTH_LONG).show();
            openLoginActivity();
        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void orderItemClicked(int position) {
         RealOrder realOrder = orders.get(position);
        Intent intent = OrderDetailActivity.newInstance(getContext(), realOrder);
        startActivity(intent);
    }
}
