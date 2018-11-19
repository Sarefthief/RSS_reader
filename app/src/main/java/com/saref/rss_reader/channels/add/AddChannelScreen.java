package com.saref.rss_reader.channels.add;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.saref.rss_reader.R;
import com.saref.rss_reader.channels.parser.CheckChannelService;

import java.net.MalformedURLException;
import java.net.URL;

class AddChannelScreen
{
    private Activity activity;
    private Button addChannelButton;
    private EditText channelURL;

    AddChannelScreen(Activity activity)
    {
        this.activity = activity;
        findElements();
    }

    private void findElements()
    {
        channelURL = activity.findViewById(R.id.addChannel);
        addChannelButton = activity.findViewById(R.id.addChannelButton);
        addChannelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try {
                    URL url = new URL(channelURL.getText().toString()) ;
                    activity.startService(CheckChannelService.getCheckChannelServiceIntent(activity, url));
                } catch (MalformedURLException e) {

                }
            }
        });
    }
}
