package com.android.mumo.swahilicuisine;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.mumo.swahilicuisine.fragments.MenuFragment;

public class MenuActivity extends AppCompatActivity {

    public static final String EXTRA_RES_ID ="com.android.mumo.swahilicuisine.resId";
    public static final String EXTRA_RES_NAME ="com.android.mumo.swahilicuisine.resName";
    public static final String EXTRA_RES_IMAGEURL ="com.android.mumo.swahilicuisine.resImageUrl";
    public static final String EXTRA_RES_FEE ="com.android.mumo.swahilicuisine.res_del_fee";
    public static final String EXTRA_RES_TIME ="com.android.mumo.swahilicuisine.res_del_time";


    public static Intent newIntent(Context packageContext,
                                   int resId, String name, String url, String time, Double deliveryFee){
        Intent intent = new Intent(packageContext, MenuActivity.class);
        intent.putExtra(EXTRA_RES_ID, resId);
        intent.putExtra(EXTRA_RES_NAME, name);
        intent.putExtra(EXTRA_RES_IMAGEURL, url);
        intent.putExtra(EXTRA_RES_FEE, deliveryFee);
        intent.putExtra(EXTRA_RES_TIME, time);

        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if(getIntent() != null){
            int resId = getIntent().getIntExtra(EXTRA_RES_ID,0);
            String resName = getIntent().getStringExtra(EXTRA_RES_NAME);
            String resUrl = getIntent().getStringExtra(EXTRA_RES_IMAGEURL);
            String resTime = getIntent().getStringExtra(EXTRA_RES_TIME);
            double fee = getIntent().getDoubleExtra(EXTRA_RES_FEE, 0.0);

            MenuFragment fragment = MenuFragment.newInstance(resId,resName, resUrl, resTime, fee);

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
