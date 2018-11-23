package com.saref.rss_reader.database;

import android.provider.BaseColumns;

public final class NewsContract implements BaseColumns
{
    private NewsContract(){}

    public static final String TABLE_NAME = "news";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_LINK = "link";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
    public static final int MAX_ROWS_COUNT = 1000;
}
