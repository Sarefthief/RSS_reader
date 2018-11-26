package com.saref.rss_reader;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.channels.ChannelsActivity;
import com.saref.rss_reader.channels.SettingsActivity;
import com.saref.rss_reader.channels.add.AddChannelActivity;
import com.saref.rss_reader.news.NewsActivity;

public final class ChoseStartScreenActivity extends AppCompatActivity
{
    public final static String LAST_VISITED_SCREEN = "LAST_VISITED_SCREEN";
    public final static String NEWS_SCREEN_IS_LAST = "NEWS_SCREEN_IS_LAST";
    public final static String NEWS_SCREEN_LINK_KEY = "NEWS_SCREEN_LINK_KEY";
    public final static String NEWS_SCREEN_TITLE_KEY = "NEWS_SCREEN_TITLE_KEY";
    public final static String ADD_CHANNEL_SCREEN_IS_LAST = "ADD_CHANNEL_SCREEN_IS_LAST";
    public final static String CHANNELS_SCREEN_IS_LAST = "CHANNELS_SCREEN_IS_LAST";

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String lastScreen = sharedPreferences.getString(LAST_VISITED_SCREEN, CHANNELS_SCREEN_IS_LAST);
        switch (lastScreen)
        {
            case NEWS_SCREEN_IS_LAST:
                final String channelLink = sharedPreferences.getString(NEWS_SCREEN_LINK_KEY, "");
                final String channelTitle = sharedPreferences.getString(NEWS_SCREEN_TITLE_KEY, "");
                if (!"".equals(channelLink) && !"".equals(channelTitle))
                {
                    startActivity(NewsActivity.getNewsActivityIntent(this, new Channel(channelTitle, channelLink)));
                }
                break;
            case ADD_CHANNEL_SCREEN_IS_LAST:
                startActivity(AddChannelActivity.getAddChannelIntent(this));
                break;
            case CHANNELS_SCREEN_IS_LAST:
                startActivity(ChannelsActivity.getChannelsActivityIntent(this));
                break;
            default:
                startActivity(ChannelsActivity.getChannelsActivityIntent(this));
                break;
        }
        finish();
    }
}
