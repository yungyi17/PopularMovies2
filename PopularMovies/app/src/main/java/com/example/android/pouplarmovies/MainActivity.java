package com.example.android.pouplarmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.android.pouplarmovies.utilities.NetworkUtils;
import com.example.android.pouplarmovies.utilities.ParseJsonDataUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 3377;
    private String parameterPath;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        parameterPath = sharedPreferences.getString(getString(R.string.pref_popular_key),
                getString(R.string.pref_popular_value));

        Log.d(TAG, "Path Parameter in onCreate: " + parameterPath);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        // getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        android.support.v4.app.LoaderManager
                .getInstance(this).initLoader(MOVIE_LOADER_ID, null, this);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {

            /* This String variable will hold and help cache the movie data */
            String movieData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (movieData != null) {
                    deliverResult(movieData);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {
                Log.d(TAG, "Path Parameter in loadInBackground: " + parameterPath);
                
                if (parameterPath.equals("favorite")) {
                    return null;
                } else {
                    URL getUrl = NetworkUtils.buildUrl(parameterPath);
                    String getJsonData;

                    try {
                        Log.d(TAG, "getURL: " + getUrl);
                        getJsonData = NetworkUtils.getResponseFromHttpUrl(getUrl);
                        ParseJsonDataUtils.getMovieStringsFromJson(getJsonData);

                        return parameterPath;
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException: " + e.getMessage());
                        return null;
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception: " + e.getMessage());
                        return null;
                    }
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                super.deliverResult(data);
                movieData = data;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (s != null) {
            Intent intent = new Intent(this, MovieActivity.class);
            startActivity(intent);
        } else {
            if (parameterPath.equals("favorite")) {
                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
                startActivity(favoriteIntent);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        android.support.v4.app.LoaderManager
                .getInstance(this).restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_popular_key))) {
            sharedPreferences.getString(getString(R.string.pref_popular_key),
                    getString(R.string.pref_popular_value));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
