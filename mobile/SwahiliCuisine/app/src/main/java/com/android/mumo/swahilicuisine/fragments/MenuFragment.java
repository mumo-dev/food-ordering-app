package com.android.mumo.swahilicuisine.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.adapters.MenuAdapter;
import com.android.mumo.swahilicuisine.model.Menu;
import com.android.mumo.swahilicuisine.model.Order;
import com.android.mumo.swahilicuisine.model.OrderItem;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuFragment extends Fragment implements MenuAdapter.OnMenuSelectedListener {

    private static final String ARG_REST_ID = "restId";
    private static final String ARG_REST_TIME = "restTime";
    private static final String ARG_REST_FEE = "restfee";
    private static final String ARG_REST_NAME = "restName";
    private static final String ARG_REST_IMAGE_URL = "restImageUrl";


    private int mRestId;
    private String mDeliveryTime;
    private String mRestuarantName;
    private String mRestaurantImageUrl;
    private Double mDeliveryFee;

    private ImageView restaurantImage;
    private TextView mRestNameTextView;
    private TextView mRestFeeTextView;
    private TextView mRestTimeTextView;

    private RecyclerView recyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView errorTv;
    private LinearLayout linearLayout;
    private TextView custOrderView;

    private MenuAdapter menuAdapter;
    private Order order;

    private List<Menu> menus;


    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(int restId, String name, String url, String time, Double fee) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_REST_ID, restId);
        args.putString(ARG_REST_TIME, time);
        args.putString(ARG_REST_NAME, name);
        args.putString(ARG_REST_IMAGE_URL, url);
        args.putDouble(ARG_REST_FEE, fee);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRestId = getArguments().getInt(ARG_REST_ID);
            mDeliveryTime = getArguments().getString(ARG_REST_TIME);
            mRestuarantName = getArguments().getString(ARG_REST_NAME);
            mRestaurantImageUrl = getArguments().getString(ARG_REST_IMAGE_URL);
            mDeliveryFee = getArguments().getDouble(ARG_REST_FEE);
        }

        menuAdapter = new MenuAdapter(getActivity(), this);
        order = new Order();
        order.setDeliveryCost(mDeliveryFee);
        order.setDeliveryTime(mDeliveryTime);
        order.setItems(new ArrayList<OrderItem>());
//        order
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        restaurantImage = view.findViewById(R.id.rest_image);
        mRestNameTextView = view.findViewById(R.id.restaurant_name);
        mRestTimeTextView = view.findViewById(R.id.restaurant_time);
        mRestFeeTextView = view.findViewById(R.id.delivery_fee);
        custOrderView = view.findViewById(R.id.cust_order);

        if (order.getItems() == null || order.getItems().size() == 0) {
            custOrderView.setText("No items on cart yet");
            custOrderView.setVisibility(View.GONE);
        }

        custOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
                Bundle args = new Bundle();
                args.putSerializable("order", order);
                orderDetailsFragment.setArguments(args);
                orderDetailsFragment.show(getActivity().getSupportFragmentManager(), orderDetailsFragment.getTag());
            }
        });

        mRestNameTextView.setText(mRestuarantName);
        mRestTimeTextView.setText(mDeliveryTime);
        mRestFeeTextView.setText("Delivery fee " + mDeliveryFee + " Ksh");
        Glide.with(getActivity())
                .load(RetrofitClient.BASE_URL + "images/" + mRestaurantImageUrl)
                .into(restaurantImage);

        //set up recyclerview
        recyclerView = view.findViewById(R.id.menu_recycler_view);
        mLoadingIndicator = view.findViewById(R.id.pg_loading_indicator);
        errorTv = view.findViewById(R.id.tv_error);
        linearLayout = view.findViewById(R.id.view_empty_menu);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(menuAdapter);

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
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        uiLoading();
        api.fetchMenu(mRestId).enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                uiLoaded();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        menus = response.body();
                        menuAdapter.setMenuList(menus);
                    }
                    if (response.body() == null || response.body().size() == 0) {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                uiError(t.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onMenuItemSelected(Menu menu) {

        boolean menuInCurrentOrder = false;
        List<OrderItem> currentOrderItems = order.getItems();
        OrderItem newOrderItem = new OrderItem(menu, 1);
        int index = 0;
        for (OrderItem orderItem : currentOrderItems) {
            if (orderItem.getMenu().getId() == menu.getId()) {
                newOrderItem.setQuantity(orderItem.getQuantity() + 1);
                menuInCurrentOrder = true;
                break;
            }
            index++;
        }
        //if menu in current order, update quantity
        if (!menuInCurrentOrder) {
            currentOrderItems.add(newOrderItem);
        } else {
            //
            currentOrderItems.set(index, newOrderItem);
        }

        //get all items in the order calculate the total cost of items;
        double totalcost = 0;
        for (OrderItem item : currentOrderItems) {
            totalcost += (item.getQuantity() * item.getMenu().getPrice());
        }
        int totalItems = 0;
        for (OrderItem item : currentOrderItems) {
            totalItems += item.getQuantity();
        }

        order.setItems(currentOrderItems);
        if (order.getItems() == null || order.getItems().size() == 0) {
            custOrderView.setText("No items on cart yet");
            custOrderView.setVisibility(View.GONE);
        } else {
            custOrderView.setText(totalItems + " items.   VIEW ORDER   " + totalcost + "Kshs");
            custOrderView.setVisibility(View.VISIBLE);
        }

        //update selected number of items
        for (Menu menu1: menus){
            if(menu1.getId()== menu.getId()){
                menu.setNoOfItems(menu.getNoOfItems()+1);
                menuAdapter.setMenuList(menus);
                break;
            }
        }

//        order.setItems(n);
    }
}
