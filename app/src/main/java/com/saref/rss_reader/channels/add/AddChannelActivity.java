package com.saref.rss_reader.channels.add;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.saref.rss_reader.R;

public class AddChannelActivity extends AppCompatActivity
{
    private AddChannelScreen addChannelScreen;
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        addChannelScreen = new AddChannelScreen(this);
    }

    public static Intent getAddChannelIntent(final Activity activity)
    {
        return new Intent(activity, AddChannelActivity.class);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        addChannelScreen.onResume();
    }

}
