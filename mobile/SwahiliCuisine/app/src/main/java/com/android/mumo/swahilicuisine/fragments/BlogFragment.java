package com.android.mumo.swahilicuisine.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.android.mumo.swahilicuisine.BlogDetailActivity;
import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.adapters.BlogAdapter;
import com.android.mumo.swahilicuisine.model.Blog;
import com.android.mumo.swahilicuisine.model.RealOrder;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogFragment extends Fragment implements BlogAdapter.OnBlogItemClicked {

    private RecyclerView recyclerView;
    private BlogAdapter mAdapter;

    private ProgressBar mLoadingIndicator;
    private TextView errorTv;
    private LinearLayout linearLayout;

    private List<Blog> blogList;


    public BlogFragment() {
    }

    public static BlogFragment newInstance() {
        BlogFragment fragment = new BlogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blogList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blog_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.blog_list);
        mLoadingIndicator = view.findViewById(R.id.pg_loading_indicator);
        errorTv = view.findViewById(R.id.tv_error);
        linearLayout = view.findViewById(R.id.view_empty_menu);

        mAdapter = new BlogAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(mAdapter);

        fetchBlogPosts();

        errorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchBlogPosts();
            }
        });


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


    private void fetchBlogPosts() {
            ApiService service = RetrofitClient.getClient().create(ApiService.class);
            uiLoading();
            service.fetchBlogPosts().enqueue(new Callback<List<Blog>>() {
                @Override
                public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                    uiLoaded();
                    if (response.isSuccessful()) {
                        if(response.body() != null) {
                            blogList = response.body();
                            mAdapter.setBlogList(response.body());
                        }
                        if (response.body() == null || response.body().size() == 0) {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Blog>> call, Throwable t) {
                    uiError(t.getMessage());
                }
            });

    }


    @Override
    public void onBlogItemSelected(Blog blog) {
        Intent intent = BlogDetailActivity.newIntent(getActivity(), blog);
        startActivity(intent);
    }
}
