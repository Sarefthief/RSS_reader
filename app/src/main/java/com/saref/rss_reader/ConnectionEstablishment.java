package com.saref.rss_reader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class ConnectionEstablishment
{
    private ConnectionEstablishment()
    {}

    public static HttpURLConnection openConnection(URL url) throws IOException
    {
        //final String URL_LINK = "https://lenta.ru/rss";
        final String REQUEST_METHOD = "GET";
        final int READ_TIMEOUT_IN_MILLISECONDS = 20000;
        final int CONNECT_TIMEOUT_IN_MILLISECONDS = 25000;

        //final URL url = new URL(URL_LINK);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(READ_TIMEOUT_IN_MILLISECONDS);
        connection.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLISECONDS);
        connection.setRequestMethod(REQUEST_METHOD);
        connection.setDoInput(true);
        connection.connect();

        return connection;
    }
}
