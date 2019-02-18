package com.android.mumo.swahilicuisine;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.model.Menu;
import com.android.mumo.swahilicuisine.model.Menujst;
import com.android.mumo.swahilicuisine.model.Order;
import com.android.mumo.swahilicuisine.model.OrderItem;
import com.android.mumo.swahilicuisine.model.Orderjst;
import com.android.mumo.swahilicuisine.model.User;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;
import com.android.mumo.swahilicuisine.utils.PreferenceUtils;
import com.android.mumo.swahilicuisine.utils.SendOrderStatusJobService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.pusher.pushnotifications.PushNotifications;
public class ConfirmOrderActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmOrderActivity";

    private TextView mUserNameTv, mUserPhoneTv, mUserLocationTv, mErrorTextView;
    private Button mPlaceOrderButton;
    private ProgressBar mProgressBar;



    public static final String EXTRA_FROM_COONFIRM_ORDER = "com.android.mumo.swahilicuisine.confrim.orders";
    public static final String EXTRA_FROM_CONFIRM_ORDER = "com.android.mumo.swahilicuisine.confirm.orders";

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);


        mUserNameTv = findViewById(R.id.tv_user_name);
        mUserPhoneTv = findViewById(R.id.tv_user_phone);
        mUserLocationTv = findViewById(R.id.tv_user_location);
        mErrorTextView = findViewById(R.id.tv_error);
        mPlaceOrderButton = findViewById(R.id.btn_order);
        mProgressBar = findViewById(R.id.progressBar);

        Order order = PreferenceUtils.getUserOrder(this);

         user = PreferenceUtils.getUserDetails(this);
        String town = PreferenceUtils.getLocationName(this);
        String area = PreferenceUtils.getLocationAreaName(this);
        int areaId = PreferenceUtils.getLocationAreaId(this);

        //if no order, redirect to back
        if (order == null) {
            //redirect to main activity;;
           /* Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);*/
//            onBackPressed();
            finish();
        }

        //if user not logged in, redirect to login
        if (user == null) {
            openLoginActivity();
        }

        mUserNameTv.setText("Name: " + user.getName());
        mUserPhoneTv.setText("Phone: " + user.getPhone());
        mUserLocationTv.setText("Location: " + town + " - " + area);

        for (int i = 0; i < order.getItems().size(); i++) {
            OrderItem item = order.getItems().get(i);
            if (item.getQuantity() == 0) {
                order.getItems().remove(i);

            }

        }

        int userId = user.getId();
        final String token = user.getToken();
        double cost = order.getDeliveryCost();

        final Orderjst orderjst = new Orderjst();

        orderjst.setUserId(userId);
        orderjst.setCost(cost);
        orderjst.setAreaId(areaId);

//            JSONArray menuArray = new JSONArray();
        List<Menujst> menujsts = new ArrayList<>();
        for (OrderItem item : order.getItems()) {

            Menujst menujst = new Menujst();

            menujst.setMenuId(item.getMenu().getId());
            menujst.setQuantity(item.getQuantity());
            menujst.setPrice(item.getMenu().getPrice());

            menujsts.add(menujst);
        }

        orderjst.setItems(menujsts);


        mPlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeOrder("JWT " + token, orderjst);
            }
        });

    }

    public void openLogin(View view) {
        openLoginActivity();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(EXTRA_FROM_COONFIRM_ORDER, 100);
        startActivity(intent);
    }

    private void storeOrder(String authorization, Orderjst data) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        mProgressBar.setVisibility(View.VISIBLE);
        api.storeOrder(authorization, data).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Log.i(TAG, "onResponse: " + response.message());
                if (response.isSuccessful() && response.code() == 200) {

                   //orderstatus'+order.userId
//                    scheduleANotificationService("orderstatus"+user.getId());

                    PushNotifications.start(getApplicationContext(), "f00f783a-c8e8-415b-89d7-83f906a2b258");
                    PushNotifications.subscribe("orderstatus"+user.getId());

                    PreferenceUtils.deleteOrder(ConfirmOrderActivity.this);
                    mErrorTextView.setVisibility(View.INVISIBLE);
//                    showAlertDialog();
                    Toast.makeText(ConfirmOrderActivity.this, "Order Placed successfully",
                            Toast.LENGTH_LONG).show();

                    redirectToOrdersFragment();
                }
                if (response.code() == 401) {
                    //redirect to login
                    /*mErrorTextView.setVisibility(View.VISIBLE);
                    mErrorTextView.setText(response.message() + "\n" + "Please sign up first");*/
                    Toast.makeText(ConfirmOrderActivity.this,
                            "Error saving your order\n Please Login First", Toast.LENGTH_LONG).show();
                    openLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                mProgressBar.setVisibility(View.INVISIBLE);
                mErrorTextView.setVisibility(View.VISIBLE);
                mErrorTextView.setText(t.getMessage());
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Order Placed successfully");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //redirect to track order activity;;
            }
        });

        builder.create();
        builder.show();
    }

    private void redirectToOrdersFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_FROM_CONFIRM_ORDER, "yes");
        startActivity(intent);
    }

    private void scheduleANotificationService(String key){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("key", key);

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(SendOrderStatusJobService.class)
                // uniquely identifies the job
                .setTag("order_notification")
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now

                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)

                // constraints that need to be satisfied for the job to run

                .setExtras(myExtrasBundle)
                .build();

        dispatcher.mustSchedule(myJob);
    }

}
