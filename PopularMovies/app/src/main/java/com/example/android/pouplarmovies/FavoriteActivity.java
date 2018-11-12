package com.example.android.pouplarmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.pouplarmovies.data.AppDatabase;
import com.example.android.pouplarmovies.data.FavoriteEntry;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity
        implements FavoriteAdapter.ItemClickListener {

    private static final String TAG = FavoriteActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.favorite_recycler_view);

        // Set the layout for the RecyclerView to be a grid layout, which measures
        // and positions items within a RecyclerView into a grid list
        GridLayoutManager layoutManager = new GridLayoutManager(this,
                2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new FavoriteAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        setUpViewModel();
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

    private void setUpViewModel() {
        // Initialize view model
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Observe the live data in the view model
        viewModel.getFavoriteEntries().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setFavorites(favoriteEntries);
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
