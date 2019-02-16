package com.android.mumo.swahilicuisine.fragments;

import android.content.Context;
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

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.adapters.BlogAdapter;
import com.android.mumo.swahilicuisine.adapters.RecipeBookAdapter;
import com.android.mumo.swahilicuisine.model.Blog;
import com.android.mumo.swahilicuisine.model.RecipeBook;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeBooksFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecipeBookAdapter mAdapter;

    private ProgressBar mLoadingIndicator;
    private TextView errorTv;
    private LinearLayout linearLayout;


    public RecipeBooksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mAdapter = new RecipeBookAdapter();
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

}
