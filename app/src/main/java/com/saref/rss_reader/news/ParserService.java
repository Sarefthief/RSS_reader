package com.saref.rss_reader.news;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ParserService extends Service
{
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new FetchFeedTask().execute((Void) null);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static Intent getParserServiceIntent(Activity activity){
        return new Intent(activity, ParserService.class);
    }
}
