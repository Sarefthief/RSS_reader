package com.saref.rss_reader.channels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.saref.rss_reader.ChoseStartScreenActivity;
import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.add.AddChannelActivity;

public final class ChannelsActivity extends AppCompatActivity
{
    public static String ADD_CHANNEL_MESSAGE = "ADD_CHANNEL_MESSAGE";
    public static String LOAD_CHANNELS_LIST_MESSAGE = "LOAD_CHANNELS_LIST_MESSAGE";

    private ChannelsScreen channelsScreen;

    public static Intent getChannelsActivityIntent(final Context context)
    {
        return new Intent(context, ChannelsActivity.class);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels_screen);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(ChoseStartScreenActivity.LAST_VISITED_SCREEN, ChoseStartScreenActivity.CHANNELS_SCREEN_IS_LAST).apply();
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
            startActivity(AddChannelActivity.getAddChannelIntent(this));
        }
        if (item.getItemId() == R.id.settingsMenuButton)
        {
            startActivity(SettingsActivity.getSettingsActivityIntent(this));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        channelsScreen.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        channelsScreen.onPause();
    }
}
