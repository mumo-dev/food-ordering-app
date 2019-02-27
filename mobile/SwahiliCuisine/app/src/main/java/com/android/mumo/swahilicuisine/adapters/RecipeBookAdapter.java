package com.android.mumo.swahilicuisine.adapters;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.model.RecipeBook;
import com.android.mumo.swahilicuisine.services.RetrofitClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;


public class RecipeBookAdapter extends RecyclerView.Adapter<RecipeBookAdapter.ViewHolder> {



    private List<RecipeBook> mRecipeBooks;
    private Context mContext;


    OnBackgroundDownloadRequest mListener;

    public RecipeBookAdapter(Context context, OnBackgroundDownloadRequest downloadRequest) {
        mRecipeBooks = new ArrayList<>();
        mContext = context;
        mListener = downloadRequest;
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
        public TextView mDownload;

        public RecipeBook mItem;

        public ViewHolder(View view) {
            super(view);

            mTitleView = (TextView) view.findViewById(R.id.book_title);
            mDateView = (TextView) view.findViewById(R.id.book_date);
            mDescView = (TextView) view.findViewById(R.id.book_description);
            mDownload = (TextView) view.findViewById(R.id.book_download);

            mDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, mItem.getDownloadUrl(),Toast.LENGTH_LONG);
                    mListener.onstartDownload(mItem.getDownloadUrl(), mItem.getTitle());
                }
            });
        }

        public void bind(RecipeBook book) {
            mItem = book;
            mTitleView.setText(book.getTitle());
            mDateView.setText("Posted on " + book.getUpdatedAt());
            mDescView.setText(book.getDescription());
        }




    }

    public interface OnBackgroundDownloadRequest {
        void onstartDownload(String url, String bookName);
    }
}
