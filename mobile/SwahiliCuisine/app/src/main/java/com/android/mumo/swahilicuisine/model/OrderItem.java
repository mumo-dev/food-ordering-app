package com.android.mumo.swahilicuisine.model;

import java.io.Serializable;

public class OrderItem  implements Serializable {
    private Menu menu;
    private int quantity;

    public OrderItem(Menu menu, int quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
