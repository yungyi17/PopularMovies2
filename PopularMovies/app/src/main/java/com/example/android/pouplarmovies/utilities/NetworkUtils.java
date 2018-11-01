package com.example.android.pouplarmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private final static String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
    private final static String PARAM_QUERY = "api_key";
    private final static String apiKey = "";

    public static URL buildUrl(String paramPath) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(paramPath)
                .appendQueryParameter(PARAM_QUERY, apiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL Exception: " + e.getMessage());
        }

        return url;
    }

    // For Stage 2: Trailers, Reviews, and Favorites
    public static URL buildUrlWithMovieId(String movieId, String paramPath) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(paramPath)
                .appendQueryParameter(PARAM_QUERY, apiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL for Movie ID: " + e.getMessage());
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
