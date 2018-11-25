package com.saref.rss_reader.channels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.saref.rss_reader.R;

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
                    if(getString(R.string.alarmCheckBoxPreferenceKey).equals(key)){

                    }
                    if(getString(R.string.alarmTimerPreferenceKey).equals(key))
                    {

                    }
                }
            };


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
}
