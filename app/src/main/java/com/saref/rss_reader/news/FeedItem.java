package com.saref.rss_reader.news;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public final class FeedItem implements Parcelable
{
    private String title;
    private String link;
    private String description;

    FeedItem(String title, String link, String description)
    {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    FeedItem(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        title = data[0];
        link = data[1];
        description = data[2];
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] { title, link, description });
    }

    public static final Parcelable.Creator<FeedItem> CREATOR = new Parcelable.Creator<FeedItem>() {

        @Override
        public FeedItem createFromParcel(Parcel source) {
            return new FeedItem(source);
        }

        @Override
        public FeedItem[] newArray(int size) {
            return new FeedItem[size];
        }
    };
}
