package com.android.mumo.swahilicuisine;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.adapters.MenuAdpater;
import com.android.mumo.swahilicuisine.model.Menu;
import com.android.mumo.swahilicuisine.model.MenuApiResponse;
import com.android.mumo.swahilicuisine.services.MenuService;
import com.android.mumo.swahilicuisine.services.RetrofitApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;
import ru.alexbykov.nopaginate.paginate.NoPaginate;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuAdpater menuAdpater;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
//        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(decoration);

        menuAdpater = new MenuAdpater(this);
        recyclerView.setAdapter(menuAdpater);
        fetchMenuItem();

    }


    private void fetchMenuItem() {
        MenuService menuServiceApi = RetrofitApi.getInstance().create();
        menuServiceApi.fetchMenus().enqueue(new Callback<MenuApiResponse>() {
            @Override
            public void onResponse(Call<MenuApiResponse> call, Response<MenuApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    List<Menu> menuList = response.body().getData();
                    int total = response.body().getTotal();
                    Log.i(TAG, "onResponse: "+ response.body());
                    boolean hasMore = response.body().isHasMore();
                    Toast.makeText(MainActivity.this, "Total Items: "+total, Toast.LENGTH_LONG).show();
                    menuAdpater.setMenuList(menuList);
                } else {
//                   No items in db;;

                }
            }

            @Override
            public void onFailure(Call<MenuApiResponse> call, Throwable t) {
// handle error accordingly
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setPagination(){
        NoPaginate noPaginate = NoPaginate.with(recyclerView)
                .setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                    }
                }).build();

//        noPaginate.showLoading(true);
//        noPaginate.s
    };

}
