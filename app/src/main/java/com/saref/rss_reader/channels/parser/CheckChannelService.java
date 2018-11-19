package com.saref.rss_reader.channels.parser;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.saref.rss_reader.news.parser.FeedParserService;

import java.net.URL;

public final class CheckChannelService extends Service
{
    public static final String CHECK_CHANNEL_EXTRA_URL = "extra url";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public static Intent getCheckChannelServiceIntent(final Activity activity, URL url)
    {
        Intent intent = new Intent();
        intent.putExtra(CHECK_CHANNEL_EXTRA_URL, url);
        return new Intent(activity, CheckChannelService.class);
    }
}
