package com.saref.rss_reader.news;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.saref.rss_reader.R;

import java.util.ArrayList;

public final class NewsScreen
{
    private Activity activity;
    private Button checkButton;
    private TextView testCheckView;
    private ArrayList itemList;

    NewsScreen (final Activity activity)
    {
        this.activity = activity;
        findViewComponents();
    }

    private void findViewComponents()
    {
        testCheckView = activity.findViewById(R.id.Test);
        checkButton = activity.findViewById(R.id.button2);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startService(ParserService.getParserServiceIntent(activity));
            }
        });
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                itemList = (ArrayList) intent.getSerializableExtra(activity.getString(R.string.LIST_NAME));
        }
        };
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter(activity.getString(R.string.SEND_ITEM_LIST_MESSAGE)));

    }


}
