package com.android.mumo.swahilicuisine.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.model.RealOrder;
import com.android.mumo.swahilicuisine.model.RealOrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder> {

    private Context mContext;
    private List<RealOrderItem> orderItemList;

    public OrderItemAdapter(Context context) {
        mContext = context;
        orderItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_detail_item, viewGroup, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orderItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public void setOrderList(List<RealOrderItem> orderList) {
        this.orderItemList = orderList;
        notifyDataSetChanged();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder  {

        TextView mNameTv, mQtyTv, mPriceTv;



        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTv = itemView.findViewById(R.id.menu_name);
            mQtyTv = itemView.findViewById(R.id.menu_quantity);
            mPriceTv = itemView.findViewById(R.id.menu_price);
        }

        public void bind(RealOrderItem order) {
            mNameTv.setText(order.getMenuName());
            mQtyTv.setText("x "+ order.getQuantity());
            mPriceTv.setText(String.valueOf(order.getPrice()));
        }

    }


}
