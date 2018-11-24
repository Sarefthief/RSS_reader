package com.saref.rss_reader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

public final class DatabaseManager
{
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DatabaseManager instance;
    private static RssReaderDbHelper dbHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized DatabaseManager getInstance(final Context context)
    {
        if (instance == null)
        {
            instance = new DatabaseManager();
            dbHelper = new RssReaderDbHelper(context);
        }

        return instance;
    }

    public synchronized SQLiteDatabase openWritableDatabase()
    {
        if (mOpenCounter.incrementAndGet() == 1)
        {
            mDatabase = dbHelper.getWritableDatabase();
        }

        return mDatabase;
    }

    public synchronized SQLiteDatabase openReadableDatabase()
    {
        if (mOpenCounter.incrementAndGet() == 1)
        {
            mDatabase = dbHelper.getReadableDatabase();
        }

        return mDatabase;
    }

    public synchronized void closeDatabase()
    {
        if (mOpenCounter.decrementAndGet() == 0)
        {
            mDatabase.close();
        }
    }
}
