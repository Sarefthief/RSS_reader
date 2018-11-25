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
import android.widget.Toast;

import com.saref.rss_reader.LifeCycleInterface;
import com.saref.rss_reader.R;
import com.saref.rss_reader.alarms.SetAlarmsService;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.channels.ChannelsActivity;
import com.saref.rss_reader.channels.parser.CheckChannelService;
import com.saref.rss_reader.alarms.AlarmReceiver;

import java.net.MalformedURLException;
import java.net.URL;

final class AddChannelScreen implements LifeCycleInterface
{
    private Activity activity;
    private EditText channelUrlField;
    private boolean isClicked = false;


    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            if (AddChannelActivity.ADD_CHANNEL_ERROR.equals(intent.getAction()))
            {
                showToast(intent.getStringExtra(AddChannelActivity.ADD_CHANNEL_ERROR));
                isClicked = false;
            }
            if (ChannelsActivity.ADD_CHANNEL_MESSAGE.equals(intent.getAction()))
            {
                activity.startService(SetAlarmsService.getAlarmsServiceIntent(activity));
                activity.startActivity(ChannelsActivity.getChannelsActivityIntent(activity));
                activity.finish();
            }
        }
    };

    AddChannelScreen(final Activity activity)
    {
        this.activity = activity;
        findElements();
    }

    AddChannelScreen(final Activity activity, final String link)
    {
        this.activity = activity;
        findElements();
        channelUrlField.setText(link);
    }

    private void findElements()
    {
        channelUrlField = activity.findViewById(R.id.enterChannelUrl);
        final Button addChannelButton = activity.findViewById(R.id.addChannelButton);
        addChannelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                if (!isClicked)
                {
                    try
                    {
                        final URL url = new URL(channelUrlField.getText().toString());
                        if (Patterns.WEB_URL.matcher(channelUrlField.getText().toString()).matches())
                        {
                            isClicked = true;
                            activity.startService(CheckChannelService.getCheckChannelServiceIntent(activity, new Channel("", url.toString())));
                        }
                        else
                        {
                            throw new MalformedURLException();
                        }
                    }
                    catch (MalformedURLException e)
                    {
                        isClicked = false;
                        channelUrlField.setError("Test");
                    }
                }
            }
        });
    }

    private void showToast(String toastText)
    {
        Toast.makeText(activity, toastText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(ChannelsActivity.ADD_CHANNEL_MESSAGE));
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(AddChannelActivity.ADD_CHANNEL_ERROR));
        isClicked = false;
    }

    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
    }
}
