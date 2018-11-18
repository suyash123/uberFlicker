package com.example.suyash.uberflicker.core.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class HttpClient {

    private static final int TIMEOUT = 60000;
    private static final String CHARSET_NAME = "UTF-8";

    public HttpClient() {
    }

    private HttpURLConnection openConnection(String uri) throws IOException {
        URL url = new URL(uri);
        System.setProperty("http.keepAlive", "false");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        return conn;
    }

    public String searchImages(String uri) {
        HttpURLConnection conn = null;
        try {
            conn = openConnection(uri);
            conn.setRequestProperty("Charset", CHARSET_NAME);
            conn.setUseCaches(false);
            conn.setDoInput(true);

            return readStream(new BufferedInputStream(conn.getInputStream())).trim();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return null;
    }

    private String readStream(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

        } catch (SocketException | SocketTimeoutException e) {
            throw e;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    public Bitmap downloadImage(String src) {
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            connection = openConnection(src);
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                connection.disconnect();
                connection = null;
            }
        }
        return null;
    }

}
