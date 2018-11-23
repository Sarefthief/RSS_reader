package com.saref.rss_reader.channels;

import android.os.Parcel;
import android.os.Parcelable;

public final class Channel implements Parcelable
{
    private String title;
    private String link;

    public Channel(final String title, final String link)
    {
        this.title = title;
        this.link = link;
    }

    private Channel(Parcel in)
    {
        String[] data = new String[2];
        in.readStringArray(data);
        title = data[0];
        link = data[1];
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>()
    {
        @Override
        public Channel createFromParcel(final Parcel in)
        {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(final int size)
        {
            return new Channel[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeStringArray(new String[]{title, link});
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


}
