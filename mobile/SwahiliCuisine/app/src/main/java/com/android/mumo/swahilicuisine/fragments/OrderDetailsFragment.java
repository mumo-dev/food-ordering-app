package com.android.mumo.swahilicuisine.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.ConfirmOrderActivity;
import com.android.mumo.swahilicuisine.LoginActivity;
import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.adapters.OrderAdapter;
import com.android.mumo.swahilicuisine.model.Menu;
import com.android.mumo.swahilicuisine.model.Order;
import com.android.mumo.swahilicuisine.model.OrderItem;
import com.android.mumo.swahilicuisine.model.User;
import com.android.mumo.swahilicuisine.utils.PreferenceUtils;
import com.android.mumo.swahilicuisine.viewmodel.OrderViewModel;

import org.w3c.dom.Text;

import static com.android.mumo.swahilicuisine.ConfirmOrderActivity.EXTRA_FROM_COONFIRM_ORDER;

public class OrderDetailsFragment extends BottomSheetDialogFragment implements OrderAdapter.OnItemModified {

    private Order order;
    private OrderAdapter adapter;

    private static final String TAG = "OrderDetailsFragment";

    private TextView subTotalView;
    private TextView deliveryCostView;
    private TextView totalCostView;
    private RecyclerView recyclerView;
    private Button proceedBtn;


    public OrderDetailsFragment() {

    }


    private double subTotal = 0;
    private double Total = 0;

    private OrderViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (getArguments() != null) {
            order = (Order) getArguments().getSerializable("order");
        }*/

        model = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);


        adapter = new OrderAdapter(this);

    }

    private void calculateCosts() {
        subTotal = 0;
        Total = 0;
        for (OrderItem item : order.getItems()) {
            subTotal += (item.getQuantity() * item.getMenu().getPrice());
        }

        Total = subTotal + order.getDeliveryCost();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        subTotalView = view.findViewById(R.id.sub_total);
        deliveryCostView = view.findViewById(R.id.delivery_cost);
        totalCostView = view.findViewById(R.id.total_cost);
        recyclerView = view.findViewById(R.id.order_items_list);
        proceedBtn = view.findViewById(R.id.proceed_btn);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subTotal == 0) {
                    return;
                }
                //save order
                PreferenceUtils.storeUserOrder(getContext(), order);
                User user = PreferenceUtils.getUserDetails(getActivity());
                if (user == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(EXTRA_FROM_COONFIRM_ORDER, 100);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
                    startActivity(intent);
                }

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        model.getOrderLiveData().observe(this, new Observer<Order>() {
            @Override
            public void onChanged(@Nullable Order newOrder) {
                order = newOrder;
                Log.i(TAG, "order set: " + order.getItems().size());
                updateUI();
            }
        });


    }

    private void updateUI() {
        calculateCosts();
        subTotalView.setText("Kshs " + subTotal);
        totalCostView.setText("Kshs " + Total);
        deliveryCostView.setText("Kshs " + order.getDeliveryCost());
        adapter.setItems(order.getItems());
    }

    @Override
    public void itemModified(int index, String action) {
        if (action.equals("add")) {
//            Toast.makeText(getContext(), "add at item pos " + index, Toast.LENGTH_LONG).show();
            OrderItem item = order.getItems().get(index);
            item.setQuantity(item.getQuantity() + 1);
            //update current ui
            //update data in adapter
//            updateUI();
        } else if (action.equals("minus")) {
            //Toast.makeText(getContext(), "minus at item pos " + index, Toast.LENGTH_LONG).show();
            OrderItem item = order.getItems().get(index);
            if (item.getQuantity() == 1) {
                item.setQuantity(0);
//                order.getItems().remove(index);
            } else {
                item.setQuantity(item.getQuantity() - 1);
            }
//            updateUI();
        } else if (action.equals("remove")) {
            //Toast.makeText(getContext(), "remove at item pos " + index, Toast.LENGTH_LONG).show();
            OrderItem item = order.getItems().get(index);
            item.setQuantity(0);
//            order.getItems().remove(index);

//            updateUI();
        }

        model.setOrderLiveData(order);


    }


}
