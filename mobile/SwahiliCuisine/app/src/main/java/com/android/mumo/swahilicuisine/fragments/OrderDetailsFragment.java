package com.android.mumo.swahilicuisine.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.adapters.OrderAdapter;
import com.android.mumo.swahilicuisine.model.Menu;
import com.android.mumo.swahilicuisine.model.Order;
import com.android.mumo.swahilicuisine.model.OrderItem;

import org.w3c.dom.Text;

public class OrderDetailsFragment extends BottomSheetDialogFragment implements OrderAdapter.OnItemModified {

    private Order order;
    private OrderAdapter adapter;

    private TextView subTotalView;
    private TextView deliveryCostView;
    private TextView totalCostView;
    private RecyclerView recyclerView;
    private Button proceedBtn;

    public OrderDetailsFragment() {

    }

    private double subTotal = 0;
    private double Total = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable("order");
        }
        ;
        adapter = new OrderAdapter(this);

        if (order != null) {
            calculateCosts();
        }
    }
    private void calculateCosts(){
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



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(decoration);

        updateUI();

    }

    private void updateUI(){
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
            updateUI();
        } else if (action.equals("minus")) {
            //Toast.makeText(getContext(), "minus at item pos " + index, Toast.LENGTH_LONG).show();
            OrderItem item = order.getItems().get(index);
            if (item.getQuantity() == 1) {
                order.getItems().remove(index);
            }else {
                item.setQuantity(item.getQuantity() - 1);
            }
            updateUI();
        } else if (action.equals("remove")) {
            //Toast.makeText(getContext(), "remove at item pos " + index, Toast.LENGTH_LONG).show();
            OrderItem item = order.getItems().remove(index);
            updateUI();
        }

    }
}
