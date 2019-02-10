package com.android.mumo.swahilicuisine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConfirmOrderActivity extends AppCompatActivity {

    private TextView mUserNameTv, mUserPhoneTv, mUserLocationTv, mErrorTextView;
    private Button mPlaceOrderButton;
    private ProgressBar mProgressBar;

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
    }

    public void openLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
