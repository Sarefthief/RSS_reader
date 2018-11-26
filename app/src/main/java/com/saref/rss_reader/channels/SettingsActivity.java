package com.saref.rss_reader.channels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.saref.rss_reader.R;
import com.saref.rss_reader.alarms.AlarmReceiver;
import com.saref.rss_reader.alarms.SetAlarmsService;

public class SettingsActivity extends PreferenceActivity
{

    public static Intent getSettingsActivityIntent(final Context context)
    {
        return new Intent(context, SettingsActivity.class);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
        {
            if (getString(R.string.alarmCheckBoxPreferenceKey).equals(key))
            {
                final boolean check = prefs.getBoolean(getString(R.string.alarmCheckBoxPreferenceKey), true);
                if (check)
                {
                    startAlarmService();
                }
                else
                {
                    stopAlarms();
                }
            }
            if (getString(R.string.alarmTimerPreferenceKey).equals(key))
            {
                startAlarmService();
            }
        }
    };

    private void startAlarmService()
    {
        startService(SetAlarmsService.getAlarmsServiceIntent(this));
    }

    private void stopAlarms()
    {
        final AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.cancelAlarm(this);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(ChannelsActivity.getChannelsActivityIntent(this));
    }
}
