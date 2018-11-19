package com.saref.rss_reader.channels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.add.AddChannelActivity;

public final class ChannelsScreenActivity extends AppCompatActivity
{
    private ChannelsScreen channelsScreen;
    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels_screen);
        channelsScreen = new ChannelsScreen(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_channels_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.addChannel){
            if (!isClicked) {
                isClicked = true;
                startActivity(AddChannelActivity.getAddChannelIntent(this));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isClicked = false;
    }
}
