package com.android.mumo.swahilicuisine.model;

import java.util.List;

public class MenuApiResponse {
    private List<Menu> data;
    private int total;
    private boolean hasMore;

    public MenuApiResponse() {
    }

    public List<Menu> getData() {
        return data;
    }

    public void setData(List<Menu> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
