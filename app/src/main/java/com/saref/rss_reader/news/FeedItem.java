package com.saref.rss_reader.news;

import java.io.Serializable;

public final class FeedItem implements Serializable
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
}
