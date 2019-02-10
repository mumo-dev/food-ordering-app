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
    private OnMenuSelectedListener mListener;


    public MenuAdapter(Context context, OnMenuSelectedListener listener) {
        mContext = context;
        mListener = listener;
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
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView, mDescTv, mPriceTv, mNumberSelected;
        private Menu menu;


        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.menu_name);
            mDescTv = itemView.findViewById(R.id.menu_description);
            mPriceTv = itemView.findViewById(R.id.menu_price);
            mNumberSelected = itemView.findViewById(R.id.items_selected);
        }

        public void bind(Menu menu) {
            this.menu = menu;
            mNameTextView.setText(menu.getName());
            mDescTv.setText(menu.getDescription());
            mPriceTv.setText(menu.getPrice() + " Kshs");
            if(menu.getNoOfItems() == 0){
                mNumberSelected.setVisibility(View.INVISIBLE);
            }else {
                mNumberSelected.setVisibility(View.VISIBLE);
                mNumberSelected.setText(String.valueOf(menu.getNoOfItems()));
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onMenuItemSelected(this.menu);
        }
    }

    public interface OnMenuSelectedListener {
        void onMenuItemSelected(Menu menu);
    }
}
