package com.saref.rss_reader.database;

import android.provider.BaseColumns;

public final class ChannelsContract implements BaseColumns
{
    private ChannelsContract(){}

    public static final String TABLE_NAME = "channels";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_LINK = "link";
}
