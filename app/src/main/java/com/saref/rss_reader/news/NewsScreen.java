package com.saref.rss_reader.news;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.saref.rss_reader.R;

public class NewsScreen
{
    private Activity activity;
    private Button checkButton;
    private TextView testCheckView;

    NewsScreen (Activity activity)
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
    }

}
