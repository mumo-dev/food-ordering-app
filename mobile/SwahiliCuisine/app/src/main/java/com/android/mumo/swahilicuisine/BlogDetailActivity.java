package com.android.mumo.swahilicuisine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.mumo.swahilicuisine.model.Blog;

public class BlogDetailActivity extends AppCompatActivity {

    private static final String EXTRA_BLOG = "com.android.mumo.swahilicuisine.blog_dteals" ;
    private TextView mTitleTv, mDateTv;
    private WebView mContentView;

    private Blog blog;

    public static Intent newIntent(Context packageContext, Blog blog){
        Intent i = new Intent(packageContext, BlogDetailActivity.class);
        i.putExtra(EXTRA_BLOG,blog);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        mTitleTv = findViewById(R.id.blog_title);
        mDateTv = findViewById(R.id.blog_date);
        mContentView = findViewById(R.id.blog_content);

        Intent intent = getIntent();
        if(intent!= null){
            blog = (Blog) intent.getSerializableExtra(EXTRA_BLOG);
            mTitleTv.setText(blog.getTitle());
            mDateTv.setText("Posted on "+ blog.getCreatedAt());
            String htmlAsString = blog.getDescription();
            mContentView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
