package com.saref.rss_reader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RssReaderDbHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "RssReader.db";

    private static final String SQL_CREATE_CHANNELS =
            "CREATE TABLE " + ChannelsContract.TABLE_NAME + " (" +
                    ChannelsContract._ID + " INTEGER PRIMARY KEY," +
                    ChannelsContract.COLUMN_NAME_TITLE + " TEXT," +
                    ChannelsContract.COLUMN_NAME_LINK + " TEXT UNIQUE)";

    private static final String SQL_CREATE_NEWS =
            "CREATE TABLE " + NewsContract.TABLE_NAME + " (" +
                    NewsContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NewsContract.COLUMN_NAME_TITLE + " TEXT," +
                    NewsContract.COLUMN_NAME_LINK + " TEXT UNIQUE," +
                    NewsContract.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    NewsContract.COLUMN_NAME_DATE + " TEXT," +
                    NewsContract.COLUMN_NAME_CHANNEL_ID + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + NewsContract.COLUMN_NAME_CHANNEL_ID + ") REFERENCES " + ChannelsContract.TABLE_NAME + "(" + NewsContract._ID + ")" + ")"
            ;

    private static final String SQL_DELETE_CHANNELS =
            "DROP TABLE IF EXISTS " + ChannelsContract.TABLE_NAME;

    private static final String SQL_DELETE_NEWS =
            "DROP TABLE IF EXISTS " + NewsContract.TABLE_NAME;

    public RssReaderDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(SQL_CREATE_CHANNELS);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL(SQL_DELETE_CHANNELS);
        sqLiteDatabase.execSQL(SQL_DELETE_NEWS);
        sqLiteDatabase.execSQL(SQL_CREATE_CHANNELS);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS);
    }
}
