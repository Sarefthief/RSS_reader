package com.saref.rss_reader.database;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class RssReaderDbHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 7;
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
                    "FOREIGN KEY(" + NewsContract.COLUMN_NAME_CHANNEL_ID + ") REFERENCES " + ChannelsContract.TABLE_NAME + "(" + NewsContract._ID + ") ON DELETE CASCADE" + ")";

    private static final String SQL_DELETE_CHANNELS =
            "DROP TABLE IF EXISTS " + ChannelsContract.TABLE_NAME;

    private static final String SQL_DELETE_NEWS =
            "DROP TABLE IF EXISTS " + NewsContract.TABLE_NAME;

    public RssReaderDbHelper(final Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(SQL_CREATE_CHANNELS);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase,final int i, final int i1)
    {
        sqLiteDatabase.execSQL(SQL_DELETE_CHANNELS);
        sqLiteDatabase.execSQL(SQL_DELETE_NEWS);
        sqLiteDatabase.execSQL(SQL_CREATE_CHANNELS);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS);
    }

    @Override
    public void onOpen(final SQLiteDatabase db)
    {
        super.onOpen(db);
        if (!db.isReadOnly())
        {
            setForeignKeyConstraintsEnabled(db);
        }
    }

    private void setForeignKeyConstraintsEnabled(final SQLiteDatabase db)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        }
        else
        {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(final SQLiteDatabase db)
    {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(final SQLiteDatabase db)
    {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
