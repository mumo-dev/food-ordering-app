package com.android.mumo.swahilicuisine.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.model.Menu;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MenuAdpater extends RecyclerView.Adapter<MenuAdpater.MenuViewHolder> {

    private Context mContext;
    private List<Menu> menuList;

    public void setMenuList(List<Menu> menuList) {

        if(this.menuList == null){
            menuList = new ArrayList<>();
        }
        this.menuList.addAll(menuList);
        notifyDataSetChanged();
    }

    public MenuAdpater(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.menu_item_layout, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {

        holder.bind(menuList.get(position));
    }

    @Override
    public int getItemCount() {
        if (menuList == null) return 0;
        return menuList.size();
    }

    public  class MenuViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView FoodNameTv;
        private TextView FoodPriceTv;
        private TextView RestaurantNameTv;
        private TextView deliveryTime;


        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            FoodNameTv = itemView.findViewById(R.id.menu_name);
            FoodPriceTv = itemView.findViewById(R.id.food_price);
            RestaurantNameTv = itemView.findViewById(R.id.menu_res);
            deliveryTime = itemView.findViewById(R.id.devlivery_time);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Menu menu) {
            FoodNameTv.setText(menu.getName());
            FoodPriceTv.setText("Price: Kshs " + menu.getPrice());
            RestaurantNameTv.setText("By: " + menu.getRestaurantName());
//            deliveryTime.setText("| Delivery Time: "+ menu.get);
            Glide.with(mContext)
                    .load("http://10.0.3.2:3000/images/"+menu.getImageUrl())
                    .into(imageView);
        }
    }
}
