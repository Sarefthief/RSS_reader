package com.saref.rss_reader.news.parser;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.saref.rss_reader.news.NewsScreen;

public final class FeedParserService extends Service
{

    BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            stopSelf();
        }
    };

    @Override
    public int onStartCommand(final Intent intent,final int flags,final int startId)
    {
        new Thread(new FetchFeedThread(this)).start();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(NewsScreen.STOP_SERVICE_MESSAGE) );

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent)
    {
        return null;
    }

    public static Intent getParserServiceIntent(final Activity activity)
    {
        return new Intent(activity, FeedParserService.class);
    }
}
