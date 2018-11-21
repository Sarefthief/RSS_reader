package com.saref.rss_reader.channels;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;

public final class Channel implements Serializable
{
    private String title;
    private URL link;

    public Channel(final String title,final URL link)
    {
        this.title = title;
        this.link = link;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public URL getLink()
    {
        return link;
    }

    public void setLink(final URL link)
    {
        this.link = link;
    }
}
