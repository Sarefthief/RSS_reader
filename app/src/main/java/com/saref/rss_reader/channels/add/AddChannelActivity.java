package com.saref.rss_reader.channels.add;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.saref.rss_reader.ChoseStartScreenActivity;
import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.ChannelsActivity;

public final class AddChannelActivity extends AppCompatActivity
{
    public static final String ADD_CHANNEL_ERROR = "ADD_CHANNEL_ERROR";
    static final String EDIT_TEXT_INPUT_STRING = "EDIT_TEXT_INPUT_STRING";

    private AddChannelScreen addChannelScreen;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(ChoseStartScreenActivity.LAST_VISITED_SCREEN, ChoseStartScreenActivity.ADD_CHANNEL_SCREEN_IS_LAST).apply();
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction()))
        {
            addChannelScreen = new AddChannelScreen(this, intent.getDataString());
        }
        else
        {
            addChannelScreen = new AddChannelScreen(this);
        }
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static Intent getAddChannelIntent(final Context context)
    {
        return new Intent(context, AddChannelActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            addChannelScreen.saveInput();
            startActivity(ChannelsActivity.getChannelsActivityIntent(this));
            finish();
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
    protected void onPause()
    {
        super.onPause();
        addChannelScreen.onPause();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        addChannelScreen.saveInput();
        startActivity(ChannelsActivity.getChannelsActivityIntent(this));
    }
}
