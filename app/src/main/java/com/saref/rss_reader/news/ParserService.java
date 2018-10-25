package com.saref.rss_reader.news;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

public final class ParserService extends Service
{
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent,final int flags,final int startId) {
        new Thread(new FetchFeedThread(this)).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    static Intent getParserServiceIntent(final Activity activity){
        return new Intent(activity, ParserService.class);
    }
}
