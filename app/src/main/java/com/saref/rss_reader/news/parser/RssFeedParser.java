package com.saref.rss_reader.news.parser;

import android.util.Xml;

import com.saref.rss_reader.news.FeedItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

final class RssFeedParser
{
    private final String ATOM_ITEM_NAME = "entry";
    private final String RSS_ITEM_NAME = "item";

    ArrayList<FeedItem> parse(final InputStream in) throws XmlPullParserException, IOException
    {
        final XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();

        return readFeed(parser);
    }

    private ArrayList<FeedItem> readFeed(final XmlPullParser parser) throws XmlPullParserException, IOException
    {
        ArrayList<FeedItem> feedItems = new ArrayList<>();

        while (XmlPullParser.END_DOCUMENT != parser.next()) {
            int eventType = parser.getEventType();

            String name = parser.getName();
            if(null == name)
                continue;

            if (XmlPullParser.START_TAG == eventType) {
                if(RSS_ITEM_NAME.equalsIgnoreCase(name) || ATOM_ITEM_NAME.equalsIgnoreCase(name)) {
                    feedItems.add(readItem(parser));
                }
            }
        }

        return feedItems;
    }

    private FeedItem readItem(final XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String title = "";
        String description = "";
        String link = "";

        final String TITLE_NAME = "title";
        final String LINK_NAME = "link";
        final String RSS_DESCRIPTION_NAME = "description";
        final String ATOM_DESCRIPTION_NAME = "summary";

        parser.next();
        while (!RSS_ITEM_NAME.equalsIgnoreCase(parser.getName()) && !ATOM_ITEM_NAME.equalsIgnoreCase(parser.getName())){
            if (XmlPullParser.START_TAG != parser.getEventType()) {
                parser.next();
                continue;
            }
            String name = parser.getName();

            if (null != name && XmlPullParser.START_TAG == parser.getEventType()){
                if (TITLE_NAME.equalsIgnoreCase(name)) {
                    title = readText(parser);
                } else if (LINK_NAME.equalsIgnoreCase(name))  {
                    link = readText(parser);
                } else if (RSS_DESCRIPTION_NAME.equalsIgnoreCase(name) || ATOM_DESCRIPTION_NAME.equalsIgnoreCase(name)) {
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
        if (XmlPullParser.TEXT == parser.next()) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }
}
