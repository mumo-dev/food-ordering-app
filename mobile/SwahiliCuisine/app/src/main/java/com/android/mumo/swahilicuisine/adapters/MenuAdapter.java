package com.android.mumo.swahilicuisine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context mContext;
    private List<Menu> menuList;

    public MenuAdapter(Context context) {
        mContext = context;
        menuList = new ArrayList<>();
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

    public void setMenuList(List<Menu> menuList) {
        this.menuList.addAll(menuList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView, mDescTv, mPriceTv;
        private Menu menu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.menu_name);
            mDescTv = itemView.findViewById(R.id.menu_description);
            mPriceTv = itemView.findViewById(R.id.menu_price);
        }

        public void bind(Menu menu) {
            this.menu = menu;
            mNameTextView.setText(menu.getName());
            mDescTv.setText(menu.getDescription());
            mPriceTv.setText(menu.getPrice() + " Kshs");
        }
    }
}
