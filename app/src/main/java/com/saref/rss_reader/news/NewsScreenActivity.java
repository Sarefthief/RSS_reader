package com.saref.rss_reader.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.saref.rss_reader.R;

public class NewsScreenActivity extends AppCompatActivity {
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new NewsScreen(this);
    }

}
