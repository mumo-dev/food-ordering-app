package com.android.mumo.swahilicuisine.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.adapters.BlogAdapter;
import com.android.mumo.swahilicuisine.adapters.RecipeBookAdapter;
import com.android.mumo.swahilicuisine.model.Blog;
import com.android.mumo.swahilicuisine.model.RecipeBook;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;


public class RecipeBooksFragment extends Fragment implements RecipeBookAdapter.OnBackgroundDownloadRequest {
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL = 209;
    private RecyclerView recyclerView;
    private RecipeBookAdapter mAdapter;

    private ProgressBar mLoadingIndicator;
    private TextView errorTv;
    private LinearLayout linearLayout;
    private long downloadID;

    private String mRecipeDownloadUrl;
    private String mRecipeBookTitle;


    public RecipeBooksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipebooks_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.book_list);
        mLoadingIndicator = view.findViewById(R.id.pg_loading_indicator);
        errorTv = view.findViewById(R.id.tv_error);
        linearLayout = view.findViewById(R.id.view_empty_menu);

        mAdapter = new RecipeBookAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(mAdapter);


        errorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchRecipeBooks();
            }
        });

        fetchRecipeBooks();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(onDownloadComplete);
    }

    private void uiLoading() {
        recyclerView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        errorTv.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

    }

    private void uiLoaded() {
        recyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
    }

    private void uiError(String message) {
        recyclerView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        if (message != null) {
            errorTv.setText(message + "\n Click to retry");
        }
    }


    private void fetchRecipeBooks() {
        ApiService service = RetrofitClient.getClient().create(ApiService.class);
        uiLoading();
        service.fetchRecipeBooks().enqueue(new Callback<List<RecipeBook>>() {
            @Override
            public void onResponse(Call<List<RecipeBook>> call, Response<List<RecipeBook>> response) {
                uiLoaded();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAdapter.setRecipeBooks(response.body());
                    }
                    if (response.body() == null || response.body().size() == 0) {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RecipeBook>> call, Throwable t) {
                uiError(t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (mRecipeDownloadUrl != null) {
                        startDownload(mRecipeDownloadUrl, mRecipeBookTitle);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), " Enable this permission to allow download to proceed",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onstartDownload(String url, String bookName) {

        mRecipeDownloadUrl = RetrofitClient.BASE_URL + "/files/books/" + url;
        mRecipeBookTitle = bookName;
        //  Toast.makeText(getActivity(), "Starting download "+mRecipeDownloadUrl,Toast.LENGTH_LONG).show();


        startDownload(mRecipeDownloadUrl, bookName);
    }

    private void startDownload(String url, String bookName) {

        //request permissions;;
        if (requestWritePermission()) {


            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri download_Uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(download_Uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle("Downloading Recipe Book");
            request.setDescription("Download of " + bookName +" in progress...");
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/recipe_books/"+ bookName+".pdf");

            downloadID = downloadManager.enqueue(request);

        }
    }


    private boolean requestWritePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL);
            return false;

        } else {
            return true;
        }
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_LONG).show();
            }
        }
    };


}
