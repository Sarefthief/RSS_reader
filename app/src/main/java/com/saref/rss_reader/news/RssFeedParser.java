package com.saref.rss_reader.news;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public final class RssFeedParser
{
    public ArrayList parse(final InputStream in) throws XmlPullParserException, IOException
    {
        try {
            final XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList readFeed(final XmlPullParser parser) throws XmlPullParserException, IOException
    {
        ArrayList feedItems = new ArrayList();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            int eventType = parser.getEventType();

            String name = parser.getName();
            if(name == null)
                continue;

            if (eventType == XmlPullParser.START_TAG) {
                if(name.equalsIgnoreCase("item")) {
                    feedItems.add(readItem(parser));
                }
            }
        }
        return feedItems;
    }
    private FeedItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException
    {

        String title = null;
        String description = null;
        String link = null;

        parser.next();
        while (!"item".equalsIgnoreCase(parser.getName())){
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            String name = parser.getName();

            if (name!= null && parser.getEventType() == XmlPullParser.START_TAG){
                if (name.equalsIgnoreCase("title")) {
                    title = readText(parser);
                } else if (name.equalsIgnoreCase("link")) {
                    link = readText(parser);
                } else if (name.equalsIgnoreCase("description")) {
                    description = readText(parser);
                }
            }
            parser.next();
        }

        return new FeedItem(title, link, description);
    }

    private String readText(final XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }
}
