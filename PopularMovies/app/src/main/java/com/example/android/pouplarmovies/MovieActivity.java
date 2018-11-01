package com.example.android.pouplarmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pouplarmovies.data.SaveMovieData;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity implements MovieActivityFragment.OnMovieClickListener {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private static final String GET_MOVIE_POSTER_POSITION = "poster-position";
    private int mPosition;
    private boolean mTwoPane;
    private int initCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // If mTwoPane is true, that is for tablet version
        if (findViewById(R.id.movie_linear_layout) != null) {
            initCounter++;
            mTwoPane = true;

            // Avoid displaying blank, display initial text when a tablet runs initially
            if (initCounter == 1) {
                TextView mSynopsisTextView = findViewById(R.id.synopsis_detail);
                mSynopsisTextView.setText(getString(R.string.init_label));
            }
        } else {
            mTwoPane = false;
        }

        // Configuration change like device rotation
        if (savedInstanceState != null && mTwoPane
                && savedInstanceState.containsKey(GET_MOVIE_POSTER_POSITION)) {

            int position = savedInstanceState.getInt(GET_MOVIE_POSTER_POSITION);
            mPosition = position;

            displayForTabletLayout(mPosition);
        }
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
    public void onMovieSelected(int position) {
        // Toast.makeText(this, "Position: " + position, Toast.LENGTH_LONG).show();
        mPosition = position;

        if (mTwoPane) {
            displayForTabletLayout(position);
        } else {
            String getVoteAvr = SaveMovieData.getVoteAverage().get(position);
            String getMvTitle = SaveMovieData.getMovieTitle().get(position);
            String getMvImage = SaveMovieData.getPosterImage().get(position);
            String getMvSynopsis = SaveMovieData.getMovieSynopsis().get(position);
            String getReleaseDt = SaveMovieData.getReleaseDate().get(position);
            // For Stage 2: Trailers, Reviews, and Favorites
            String getMovieId = SaveMovieData.getMovieId().get(position);
            Log.d(TAG, "Movie ID: " + getMovieId);

            Bundle mBundle = new Bundle();
            mBundle.putString("bundleVoteAverage", getVoteAvr);
            mBundle.putString("bundleMovieTitle", getMvTitle);
            mBundle.putString("bundlePosterImage", getMvImage);
            mBundle.putString("bundleMovieSynopsis", getMvSynopsis);
            mBundle.putString("bundleReleaseDate", getReleaseDt);
            // For Stage 2: Trailers, Reviews, and Favorites
            mBundle.putString("bundleMovieId", getMovieId);

            Intent detailMovieIntent = new Intent(this, DetailActivity.class);
            detailMovieIntent.putExtras(mBundle);
            startActivity(detailMovieIntent);
        }
    }

    private void displayForTabletLayout(int position) {

        ImageView mPosterImageView = findViewById(R.id.poster_image_detail);
        TextView mMovieTitleTextView = findViewById(R.id.title_detail);
        TextView mVoteAverageTextView = findViewById(R.id.vote_average_detail);
        TextView mSynopsisTextView = findViewById(R.id.synopsis_detail);
        TextView mReleaseDateTextView = findViewById(R.id.release_date_detail);

        String getVoteAvr = SaveMovieData.getVoteAverage().get(position);
        String getMvTitle = SaveMovieData.getMovieTitle().get(position);
        String getMvImage = SaveMovieData.getPosterImage().get(position);
        String getMvSynopsis = SaveMovieData.getMovieSynopsis().get(position);
        String getReleaseDt = SaveMovieData.getReleaseDate().get(position);

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + getMvImage).into(mPosterImageView);
        mMovieTitleTextView.setText(getMvTitle);
        String getAverageOfTen = String.format("%s%s", getVoteAvr, getString(R.string.of_ten));
        mVoteAverageTextView.setText(getAverageOfTen);
        mSynopsisTextView.setText(getMvSynopsis);
        mReleaseDateTextView.setText(getReleaseDt);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(GET_MOVIE_POSTER_POSITION, mPosition);
        super.onSaveInstanceState(outState);
    }
}
