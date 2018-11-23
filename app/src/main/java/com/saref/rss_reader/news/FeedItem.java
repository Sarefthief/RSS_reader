package com.saref.rss_reader.news;

import android.os.Parcel;
import android.os.Parcelable;

public final class FeedItem implements Parcelable
{
    private String title;
    private String link;
    private String description;
    private String date;

    public FeedItem(final String title, final String link, final String description, final String date)
    {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
    }

    private FeedItem(final Parcel in)
    {
        String[] data = new String[4];
        in.readStringArray(data);
        title = data[0];
        link = data[1];
        description = data[2];
        date = data[3];
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(final String link)
    {
        this.link = link;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(final String date)
    {
        this.date = date;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i)
    {
        parcel.writeStringArray(new String[]{title, link, description, date});
    }

    public static final Parcelable.Creator<FeedItem> CREATOR = new Parcelable.Creator<FeedItem>()
    {
        @Override
        public FeedItem createFromParcel(final Parcel source)
        {
            return new FeedItem(source);
        }

        @Override
        public FeedItem[] newArray(final int size)
        {
            return new FeedItem[size];
        }
    };
}


