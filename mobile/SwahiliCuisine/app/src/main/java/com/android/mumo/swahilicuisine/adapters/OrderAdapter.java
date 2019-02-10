package com.android.mumo.swahilicuisine.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.model.Menu;
import com.android.mumo.swahilicuisine.model.Order;
import com.android.mumo.swahilicuisine.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public List<OrderItem> items;
    OnItemModified mListener;

    public OrderAdapter(OnItemModified listener) {
        items = new ArrayList<>();
        mListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item_layout, null);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int i) {
        Menu menu = items.get(i).getMenu();
        String message = menu.getName() + " x " + items.get(i).getQuantity() + " -  " + menu.getPrice() + " Kshs";
        holder.itemTextView.setText(message);
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView itemTextView;
        ImageButton minusButton, addButton, removeButton;

        public OrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.menu_text);
            minusButton = itemView.findViewById(R.id.menu_minus);
            addButton = itemView.findViewById(R.id.menu_add);
            removeButton = itemView.findViewById(R.id.menu_remove);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.itemModified(position, "remove");
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.itemModified(position, "minus");
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.itemModified(position, "add");
                }
            });
        }
    }

    public interface OnItemModified {
        void itemModified(int index, String action);
    }
}
