package com.android.mumo.swahilicuisine.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.model.RecipeBook;

import java.util.ArrayList;
import java.util.List;


public class RecipeBookAdapter extends RecyclerView.Adapter<RecipeBookAdapter.ViewHolder> {


    private List<RecipeBook> mRecipeBooks;

    public RecipeBookAdapter() {
        mRecipeBooks = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipebooks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mRecipeBooks.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipeBooks.size();
    }

    public void setRecipeBooks(List<RecipeBook> mRecipeBooks) {
        this.mRecipeBooks = mRecipeBooks;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitleView;
        public TextView mDateView;
        public TextView mDescView;
        public RecipeBook mItem;

        public ViewHolder(View view) {
            super(view);

            mTitleView = (TextView) view.findViewById(R.id.book_title);
            mDateView = (TextView) view.findViewById(R.id.book_date);
            mDescView = (TextView) view.findViewById(R.id.book_description);
        }

        public void bind(RecipeBook book) {
            mItem = book;
            mTitleView.setText(book.getTitle());
            mDateView.setText("Posted on " + book.getUpdatedAt());
            mDescView.setText(book.getDescription());
        }

    }
}
