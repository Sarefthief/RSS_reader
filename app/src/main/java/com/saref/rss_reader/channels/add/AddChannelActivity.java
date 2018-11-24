package com.saref.rss_reader.channels.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.ChannelsActivity;

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
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static Intent getAddChannelIntent(final Activity activity)
    {
        return new Intent(activity, AddChannelActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            startActivity(ChannelsActivity.getChannelsActivityIntent(this));
        }
        return super.onOptionsItemSelected(item);
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
        startActivity(new Intent(this, ChannelsActivity.class));
    }
}
