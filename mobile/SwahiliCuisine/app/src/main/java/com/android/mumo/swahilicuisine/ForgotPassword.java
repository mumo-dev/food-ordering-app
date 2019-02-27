package com.android.mumo.swahilicuisine;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.model.User;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private EditText mEmailfield;
    private TextView mErrorView;
    private Button mResetButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mEmailfield = findViewById(R.id.email);
        mErrorView = findViewById(R.id.error_text);
        mResetButton = findViewById(R.id.forgot_button);
        mProgressBar = findViewById(R.id.forgot_progress);


        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailfield.getText().toString();
                if (!TextUtils.isEmpty(email)) {
                    User user = new User();
                    user.setEmail(email);


                    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                    uiLoading();
                    apiService.forgotPassword(user).enqueue(new Callback<JSONObject>() {

                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            uiLoaded();
                            if(response.isSuccessful() && response.code() ==200){
                                showAlertDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            String msg = t.getMessage() == null ? " Sending Failed. Please try again later": t.getMessage();
                            uiError(msg);
                        }
                    });
                }
            }
        });
    }

    private void  uiLoading(){
        mErrorView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void  uiLoaded(){
        mErrorView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }
    private void  uiError(String message){
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setText(message);
        mProgressBar.setVisibility(View.GONE);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Password reset link has been sent to your email\n ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //redirect to track order activity;;
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        builder.create();
        builder.show();
    }
}
