package com.saref.rss_reader.news.parser;

import android.util.Xml;

import com.saref.rss_reader.exceptions.WrongXmlTypeException;
import com.saref.rss_reader.news.FeedItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

final class FeedParser
{
    private final String ATOM_ITEM_NAME = "entry";
    private final String RSS_ITEM_NAME = "item";

    ArrayList<FeedItem> parse(final InputStream in) throws XmlPullParserException, IOException, WrongXmlTypeException
    {
        final XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();

        return readFeed(parser);
    }

    private ArrayList<FeedItem> readFeed(final XmlPullParser parser) throws XmlPullParserException, IOException, WrongXmlTypeException
    {
        final ArrayList<FeedItem> feedItems = new ArrayList<>();
        checkXML(parser);

        while (XmlPullParser.END_DOCUMENT != parser.next())
        {
            int eventType = parser.getEventType();
            String name = parser.getName();
            if (null == name)
            {
                continue;
            }
            if (XmlPullParser.START_TAG == eventType)
            {
                if (RSS_ITEM_NAME.equalsIgnoreCase(name) || ATOM_ITEM_NAME.equalsIgnoreCase(name))
                {
                    feedItems.add(readItem(parser));
                }
            }
        }

        return feedItems;
    }

    private FeedItem readItem(final XmlPullParser parser) throws XmlPullParserException, IOException
    {
        final String TITLE_NAME = "title";
        final String LINK_NAME = "link";
        final String RSS_DESCRIPTION_NAME = "description";
        final String ATOM_DESCRIPTION_NAME = "summary";
        final String RSS_DATE_NAME = "pubDate";
        final String ATOM_DATE_NAME = "updated";

        String title = "";
        String description = "";
        String link = "";
        String date = "";

        parser.next();
        while (!RSS_ITEM_NAME.equalsIgnoreCase(parser.getName()) && !ATOM_ITEM_NAME.equalsIgnoreCase(parser.getName()))
        {
            if (XmlPullParser.START_TAG != parser.getEventType())
            {
                parser.next();
                continue;
            }
            String name = parser.getName();

            if (null != name && XmlPullParser.START_TAG == parser.getEventType())
            {
                if (TITLE_NAME.equalsIgnoreCase(name))
                {
                    title = readText(parser);
                }
                else if (LINK_NAME.equalsIgnoreCase(name))
                {
                    link = parser.getAttributeValue(null, "href");
                    if (null == link)
                    {
                        link = readText(parser);
                    }
                }
                else if (RSS_DESCRIPTION_NAME.equalsIgnoreCase(name) || ATOM_DESCRIPTION_NAME.equalsIgnoreCase(name))
                {
                    description = readText(parser);
                }
                else if (RSS_DATE_NAME.equalsIgnoreCase(name) || ATOM_DATE_NAME.equalsIgnoreCase(name))
                {
                    date = readText(parser);
                }
            }
            parser.next();
        }

        return new FeedItem(title, link, description, date);
    }

    private String readText(final XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String result = "";
        if (XmlPullParser.TEXT == parser.next())
        {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    private void checkXML(final XmlPullParser parser) throws IOException, XmlPullParserException, WrongXmlTypeException
    {
        final String ATOM_PARENT_TAG_NAME = "feed";
        final String RSS_PARENT_TAG_NAME = "channel";
        final int QUANTITY_OF_TAGS_TO_CHECK = 4;

        boolean isRss = false;
        String name;

        for (int i = 0; i < QUANTITY_OF_TAGS_TO_CHECK; i++)
        {
            name = parser.getName();
            if (RSS_PARENT_TAG_NAME.equalsIgnoreCase(name) || ATOM_PARENT_TAG_NAME.equalsIgnoreCase(name))
            {
                isRss = true;
                break;
            }
            parser.next();
        }

        if (!isRss)
        {
            throw new WrongXmlTypeException();
        }
    }
}
