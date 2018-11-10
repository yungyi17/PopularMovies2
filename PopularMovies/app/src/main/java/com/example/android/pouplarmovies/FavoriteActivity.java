package com.example.android.pouplarmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;

import com.example.android.pouplarmovies.data.AppDatabase;
import com.example.android.pouplarmovies.data.FavoriteEntry;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity
        implements FavoriteAdapter.ItemClickListener {

    private static final String TAG = FavoriteActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mDb = AppDatabase.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.favorite_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this,
                2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new FavoriteAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actions_settings_menu_id) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveFavorites();
    }

    private void retrieveFavorites() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<FavoriteEntry> favoriteEntries = mDb.favoriteDao().loadAllFavorites();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setFavorites(favoriteEntries);
                    }
                });
            }
        });
    }

    @Override
    public void onItemClickListener(String movieId, String vAvg, String mTitle,
                                    String mPth, String mOverview, String rDate) {
        Bundle mBundle = new Bundle();
        mBundle.putString("bundleVoteAverage", vAvg);
        mBundle.putString("bundleMovieTitle", mTitle);
        mBundle.putString("bundlePosterImage", mPth);
        mBundle.putString("bundleMovieSynopsis", mOverview);
        mBundle.putString("bundleReleaseDate", rDate);
        // For Stage 2: Trailers, Reviews, and Favorites
        mBundle.putString("bundleMovieId", movieId);

        Intent favoriteDetailIntent = new Intent(this, DetailActivity.class);
        favoriteDetailIntent.putExtras(mBundle);
        startActivity(favoriteDetailIntent);
    }
}
