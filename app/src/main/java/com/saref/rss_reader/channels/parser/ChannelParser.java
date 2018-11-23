package com.saref.rss_reader.channels.parser;

import android.util.Xml;

import com.saref.rss_reader.exceptions.WrongXmlTypeException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public final class ChannelParser
{
    public String getChannelTitle(final InputStream in) throws XmlPullParserException, IOException, WrongXmlTypeException
    {
        final XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();

        return findTitle(parser);
    }

    private String findTitle(final XmlPullParser parser) throws WrongXmlTypeException, XmlPullParserException, IOException
    {
        final String TITLE_NAME = "title";
        final String RSS_ITEM_NAME = "item";
        final String ATOM_ITEM_NAME = "entry";

        checkXML(parser);
        String title = "";

        while (!ATOM_ITEM_NAME.equalsIgnoreCase(parser.getName()) && !RSS_ITEM_NAME.equalsIgnoreCase(parser.getName()))
        {
            parser.next();
            String name = parser.getName();

            if (null == name)
            {
                continue;
            }

            if (TITLE_NAME.equalsIgnoreCase(name))
            {
                if (XmlPullParser.TEXT == parser.next())
                {
                    title = parser.getText();
                    break;
                }
            }
        }

        return title;
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
