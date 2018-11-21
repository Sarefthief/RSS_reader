package com.saref.rss_reader.channels.parser;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.saref.rss_reader.ConnectionEstablishment;
import com.saref.rss_reader.WrongXmlTypeException;
import com.saref.rss_reader.channels.Channel;
import com.saref.rss_reader.channels.ChannelsScreenActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class CheckChannelService extends IntentService
{
    public static final String CHECK_CHANNEL_EXTRA_URL = "extra url";
    private Handler handler;
    private Channel channel;

    @Override
    public void onCreate()
    {
        super.onCreate();
        handler = new Handler();
    }

    public CheckChannelService()
    {
        super("CheckChannelService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(intent.hasExtra(CHECK_CHANNEL_EXTRA_URL)){
            URL channelUrl = (URL)intent.getExtras().get(CHECK_CHANNEL_EXTRA_URL);
            ChannelParser channelParser = new ChannelParser();
            try {
                HttpURLConnection connection = ConnectionEstablishment.openConnection(channelUrl);
                InputStream inputStream = connection.getInputStream();
                try {
                    channel = new Channel(channelParser.getChannelTitle(inputStream),channelUrl);
                } catch (WrongXmlTypeException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                    connection.disconnect();
                    sendBroadcast();
                }
            } catch (XmlPullParserException e) {

            } catch (IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CheckChannelService.this, "Hello Toast!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public static Intent getCheckChannelServiceIntent(final Activity activity, URL url)
    {
        Intent intent = new Intent(activity, CheckChannelService.class);
        intent.putExtra(CHECK_CHANNEL_EXTRA_URL, url);
        return intent;
    }

    private void sendBroadcast()
    {
        Intent intent = new Intent(ChannelsScreenActivity.ADD_CHANNEL_MESSAGE);
        intent.putExtra(ChannelsScreenActivity.ADD_CHANNEL_MESSAGE, channel);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
