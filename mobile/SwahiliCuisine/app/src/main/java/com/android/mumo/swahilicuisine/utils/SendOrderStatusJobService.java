package com.android.mumo.swahilicuisine.utils;

import android.util.Log;

import com.android.mumo.swahilicuisine.services.RetrofitClient;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SendOrderStatusJobService extends JobService {
   public static final String TAG = "JobService";
    Socket mSocket;

    boolean shouldCancel;
    @Override
    public boolean onStartJob(final JobParameters job) {
        //Do some work
        try {
            mSocket = IO.socket(RetrofitClient.BASE_URL);
        } catch (URISyntaxException e) {
        }
        mSocket.connect();
        Log.i(TAG, "job started... ");
        String key = (String) job.getExtras().getString("key");

        mSocket.on(key, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String data = (String) args[0];
                Log.i(TAG, "event recieved... "+data);

                //delivered
                //launch notifcation here
                NotificationUtils.sendReminderNotification(SendOrderStatusJobService.this, data);
//                Log.i(TAG, "call: ");
                if(data.equals("delivered")){
//                    shouldCancel =true;
                    jobFinished(job, false);
                }
            }
        });

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        mSocket.disconnect();
        return false; // Answers the question: "Should this job be retried?"
    }



}
