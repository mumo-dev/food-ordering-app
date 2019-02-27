package com.android.mumo.swahilicuisine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.adapters.OrderItemAdapter;
import com.android.mumo.swahilicuisine.model.RealOrder;
import com.android.mumo.swahilicuisine.model.RealOrderItem;

public class OrderDetailActivity extends AppCompatActivity {


    private TextView orderIdTv, OrderDateTv, OrderStatusTv, OrderSubTotalTv, deliveryCostTv,
            totalCostTv, cutomerName, restauranttTv;

    private RecyclerView recyclerView;


    public static final String EXTRA_ORDER = "com.android.mumo.swahilicuisine.oytres";

    public static Intent newInstance(Context packageContext, RealOrder order) {
        Intent intent = new Intent(packageContext, OrderDetailActivity.class);
        intent.putExtra(EXTRA_ORDER, order);
        return intent;
    }

    private RealOrder order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order = (RealOrder) getIntent().getSerializableExtra(EXTRA_ORDER);

        orderIdTv = findViewById(R.id.order_id);
        OrderDateTv = findViewById(R.id.order_date);
        OrderStatusTv = findViewById(R.id.order_status);
        OrderSubTotalTv = findViewById(R.id.sub_total);
        deliveryCostTv = findViewById(R.id.delivery_cost);
        totalCostTv = findViewById(R.id.total_cost);
        cutomerName = findViewById(R.id.customer_name);
        restauranttTv = findViewById(R.id.restaurant_name);

        orderIdTv.setText("#" + order.getId());
        OrderDateTv.setText(order.getCreatedAt());
        OrderStatusTv.setText(" " + order.getStatus());
        OrderSubTotalTv.setText("Kshs " + calculateSubTotal());
        deliveryCostTv.setText("Kshs " + order.getDeliveryCost());
        double total = calculateSubTotal() + order.getDeliveryCost();
        totalCostTv.setText("Kshs " + total);

        cutomerName.setText(" " + order.getDeliveryTime());
        restauranttTv.setText(" " + order.getRestaurant());


        recyclerView = findViewById(R.id.order_items_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderItemAdapter adapter = new OrderItemAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOrderList(order.getOrderItems());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private double calculateSubTotal() {
        double total = 0;
        for (RealOrderItem item : order.getOrderItems()) {
            total += item.getPrice() * item.getQuantity();
        }

        return total;
    }
}
