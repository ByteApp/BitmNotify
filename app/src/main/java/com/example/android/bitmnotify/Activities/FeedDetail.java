package com.example.android.bitmnotify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bitmnotify.R;

public class FeedDetail extends AppCompatActivity {

    TextView tvTitle;
    TextView tvContent;
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        Intent intent = getIntent();

        tvTitle = (TextView) findViewById(R.id.textView_title_detail);
        tvContent = (TextView) findViewById(R.id.textView_content_detail);
        ivImage = (ImageView) findViewById(R.id.imageView_detail);

        Bundle b = intent.getExtras();

        tvTitle.setText(b.get("title").toString());
        tvContent.setText(b.get("content").toString());
        Glide.with(this).load(b.get("image")).crossFade().into(ivImage);

    }
}
