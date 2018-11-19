package com.saref.rss_reader.channels;

import android.os.Parcel;
import android.os.Parcelable;

public final class Channel implements Parcelable
{
    private String title;
    private String link;

    public Channel(final String title,final String link)
    {
        this.title = title;
        this.link = link;
    }

    private Channel(final Parcel in)
    {
        String[] data = new String[2];
        in.readStringArray(data);
        title = data[0];
        link = data[1];
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(final Parcel parcel, int i)
    {
        parcel.writeStringArray(new String[] { title, link});
    }

    public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>()
    {
        @Override
        public Channel createFromParcel(final Parcel source)
        {
            return new Channel(source);
        }

        @Override
        public Channel[] newArray(final int size)
        {
            return new Channel[size];
        }
    };

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
}
