package com.saref.rss_reader.channels.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.saref.rss_reader.R;

public final class AddChannelActivity extends AppCompatActivity
{
    public static final String ADD_CHANNEL_ERROR = "ADD_CHANNEL_ERROR";

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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}
