package com.saref.rss_reader.channels.add;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.saref.rss_reader.LifeCycleInterface;
import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.channels.ChannelsScreenActivity;
import com.saref.rss_reader.channels.parser.CheckChannelService;

import java.net.MalformedURLException;
import java.net.URL;

class AddChannelScreen implements LifeCycleInterface
{
    private Activity activity;
    private boolean isClicked = false;

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            activity.setResult(ChannelsScreenActivity.ADD_CHANNEL_REQUEST_CODE, intent);
            activity.finish();
        }
    };

    AddChannelScreen(final Activity activity)
    {
        this.activity = activity;
        findElements();
    }

    private void findElements()
    {
        final EditText channelURL = activity.findViewById(R.id.enterChannelUrl);
        final Button addChannelButton = activity.findViewById(R.id.addChannelButton);
        addChannelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!isClicked) {
                    try {
                        final URL url = new URL(channelURL.getText().toString());
                        if (Patterns.WEB_URL.matcher(channelURL.getText().toString()).matches()) {
                            isClicked = true;
                            activity.startService(CheckChannelService.getCheckChannelServiceIntent(activity, url));
                        } else {
                            throw new MalformedURLException();
                        }
                    } catch (MalformedURLException e) {
                        isClicked = false;
                        channelURL.setError("Test");
                    }
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(ChannelsScreenActivity.ADD_CHANNEL_MESSAGE));
        isClicked = false;
    }

    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
    }
}
