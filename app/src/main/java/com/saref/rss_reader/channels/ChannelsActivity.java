package com.saref.rss_reader.channels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.add.AddChannelActivity;

public final class ChannelsActivity extends AppCompatActivity
{
    public static String ADD_CHANNEL_MESSAGE = "ADD_CHANNEL_MESSAGE";
    public static String LOAD_CHANNELS_LIST_MESSAGE = "LOAD_CHANNELS_LIST_MESSAGE";
    public static int ADD_CHANNEL_REQUEST_CODE = 322;

    private ChannelsScreen channelsScreen;
    private boolean isClicked = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels_screen);
        channelsScreen = new ChannelsScreen(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_channels_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        if (item.getItemId() == R.id.addChannelMenuButton)
        {
            if (!isClicked)
            {
                isClicked = true;
                startActivityForResult(AddChannelActivity.getAddChannelIntent(this), ADD_CHANNEL_REQUEST_CODE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode)
        {
            Channel channel = data.getParcelableExtra(ADD_CHANNEL_MESSAGE);
            channelsScreen.addChannel(channel);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isClicked = false;
        channelsScreen.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        channelsScreen.onPause();
    }
}