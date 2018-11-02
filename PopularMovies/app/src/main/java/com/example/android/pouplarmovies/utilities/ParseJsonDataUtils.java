package com.example.android.pouplarmovies.utilities;

import android.util.Log;

import com.example.android.pouplarmovies.data.SaveMovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ParseJsonDataUtils {

    private static final String TAG = ParseJsonDataUtils.class.getSimpleName();

    public static void getMovieStringsFromJson(String movieJsonStr) throws JSONException {

        final String JSON_RESULTS = "results";
        final String JSON_VOTE_AVERAGE = "vote_average";
        final String JSON_TITLE = "title";
        final String JSON_POSTER_PATH = "poster_path";
        final String JSON_OVERVIEW = "overview";
        final String JSON_RELEASE_DATE = "release_date";
        // For Stage 2: Trailers, Reviews, and Favorites
        final String JSON_MOVIE_ID = "id";

        final String JSON_MESSAGE_CODE = "cod";

        List<String> voteAverage = new ArrayList<>();
        List<String> movieTitle = new ArrayList<>();
        List<String> posterImage = new ArrayList<>();
        List<String> movieSynopsis = new ArrayList<>();
        List<String> releaseDate = new ArrayList<>();
        // For Stage 2: Trailers, Reviews, and Favorites
        List<String> movieId = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(JSON_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(JSON_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    Log.e(TAG, "ERROR: HTTP_OK");
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    Log.e(TAG, "ERROR: HTTP_NOT_FOUND, Location Invalid");
                default:
                    /* Server probably down */
                    Log.e(TAG, "ERROR: Server down!!");
            }
        }

        JSONArray movieDataArray = movieJson.optJSONArray(JSON_RESULTS);

        for (int i = 0; i < movieDataArray.length(); i++) {

            JSONObject theMovie = movieDataArray.optJSONObject(i);

            voteAverage.add(String.valueOf(theMovie.optDouble(JSON_VOTE_AVERAGE)));
            movieTitle.add(theMovie.optString(JSON_TITLE));
            posterImage.add(theMovie.optString(JSON_POSTER_PATH));
            movieSynopsis.add(theMovie.optString(JSON_OVERVIEW));
            releaseDate.add(theMovie.optString(JSON_RELEASE_DATE));
            // For Stage 2: Trailers, Reviews, and Favorites
            movieId.add(theMovie.optString(JSON_MOVIE_ID));
        }

        SaveMovieData.setVoteAverage(voteAverage);
        SaveMovieData.setMovieTitle(movieTitle);
        SaveMovieData.setPosterImage(posterImage);
        SaveMovieData.setMovieSynopsis(movieSynopsis);
        SaveMovieData.setReleaseDate(releaseDate);
        // For Stage 2: Trailers, Reviews, and Favorites
        SaveMovieData.setMovieId(movieId);
    }

    // For Stage 2: Trailers, Reviews, and Favorites
    public static List<String> getMovieTrailersFromJson(String movieTrailerJsonStr) throws JSONException {

        final String JSON_RESULTS = "results";
        final String JSON_TRAIL_KEY = "key";

        final String JSON_MESSAGE_CODE = "cod";

        List<String> trailerKey = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieTrailerJsonStr);

        /* Is there an error? */
        if (movieJson.has(JSON_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(JSON_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    Log.e(TAG, "ERROR: HTTP_OK");
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    Log.e(TAG, "ERROR: HTTP_NOT_FOUND, Location Invalid");
                default:
                    /* Server probably down */
                    Log.e(TAG, "ERROR: Server down!!");
            }
        }

        JSONArray movieDataArray = movieJson.optJSONArray(JSON_RESULTS);

        for (int i = 0; i < movieDataArray.length(); i++) {
            JSONObject theMovieTrail = movieDataArray.optJSONObject(i);
            trailerKey.add(theMovieTrail.optString(JSON_TRAIL_KEY));
        }

        return trailerKey;
    }

    // For Stage 2: Trailers, Reviews, and Favorites
    public static URL getMovieReviewsFromJson(String movieReviewsJsonStr) throws JSONException {

        final String JSON_RESULTS = "results";
        final String JSON_REVIEW_URL = "url";

        final String JSON_MESSAGE_CODE = "cod";

        String reviewUrl = null;

        JSONObject movieJson = new JSONObject(movieReviewsJsonStr);

        /* Is there an error? */
        if (movieJson.has(JSON_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(JSON_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    Log.e(TAG, "ERROR: HTTP_OK");
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    Log.e(TAG, "ERROR: HTTP_NOT_FOUND, Location Invalid");
                default:
                    /* Server probably down */
                    Log.e(TAG, "ERROR: Server down!!");
            }
        }

        JSONArray movieDataArray = movieJson.optJSONArray(JSON_RESULTS);

        JSONObject theReviewUrl = movieDataArray.optJSONObject(0);
        reviewUrl = theReviewUrl.optString(JSON_REVIEW_URL);

        URL getReviewUrl = null;

        try {
            getReviewUrl = new URL(reviewUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get a URL for a movie review");
        }


        return getReviewUrl;
    }
}
