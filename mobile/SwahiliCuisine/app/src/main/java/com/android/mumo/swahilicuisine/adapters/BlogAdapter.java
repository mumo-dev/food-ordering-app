package com.android.mumo.swahilicuisine.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.model.Blog;

import java.util.ArrayList;
import java.util.List;


public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private List<Blog> blogList;

    OnBlogItemClicked mListener;

    public BlogAdapter(OnBlogItemClicked listener) {
        blogList = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        holder.bind(blog);
    }

    public void setBlogList(List<Blog> blogList) {
        this.blogList = blogList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitleView;
        public TextView mDateView;
        public TextView mContentView;
        public TextView mReadMore;
        public Blog mItem;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mTitleView = (TextView) view.findViewById(R.id.blog_title);
            mDateView = (TextView) view.findViewById(R.id.blog_date);
            mContentView = (TextView) view.findViewById(R.id.blog_content);
            mReadMore = (TextView) view.findViewById(R.id.read_more);

            mReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onBlogItemSelected(mItem);
                }
            });
        }

        public void bind(Blog item) {
            mItem = item;

            String htmlAsString = item.getDescription();
            Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
            mTitleView.setText(item.getTitle());
            mContentView.setText(htmlAsSpanned);
            mDateView.setText("Posted on " + item.getCreatedAt());
        }

        @Override
        public void onClick(View v) {
            mListener.onBlogItemSelected(mItem);
        }
    }


    public interface OnBlogItemClicked{
        void onBlogItemSelected(Blog blog);
    }
}
