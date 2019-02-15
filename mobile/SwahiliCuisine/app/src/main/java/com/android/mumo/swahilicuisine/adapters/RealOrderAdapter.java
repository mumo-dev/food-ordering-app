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

import java.util.ArrayList;
import java.util.List;

public class RealOrderAdapter extends RecyclerView.Adapter<RealOrderAdapter.OrderViewHolder> {

    private Context mContext;
    private List<RealOrder> orderList;
    private OnOrderItemClicked mlistener;

    public RealOrderAdapter(Context context, OnOrderItemClicked listener) {
        mContext = context;
        orderList = new ArrayList<>();
        mlistener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_item, viewGroup, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setOrderList(List<RealOrder> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mIdTv, mDateTv, mStatusTv;

        RealOrder order;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mIdTv = itemView.findViewById(R.id.order_id_tv);
            mDateTv = itemView.findViewById(R.id.order_date_tv);
            mStatusTv = itemView.findViewById(R.id.order_status_tv);
        }

        public void bind(RealOrder order) {
            this.order = order;
            mIdTv.setText("#" + order.getId());
            mDateTv.setText(order.getCreatedAt());
            mStatusTv.setText(order.getStatus());
        }

        @Override
        public void onClick(View v) {
            mlistener.orderItemClicked(getAdapterPosition());
        }
    }

    public interface OnOrderItemClicked {
        void orderItemClicked(int id);
    }
}
