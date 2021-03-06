package com.example.android.pouplarmovies;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pouplarmovies.data.AppDatabase;
import com.example.android.pouplarmovies.data.FavoriteEntry;
import com.example.android.pouplarmovies.utilities.NetworkUtils;
import com.example.android.pouplarmovies.utilities.ParseJsonDataUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String[]> {

    // Constant for default task id to be used when not in update mode
    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final int MOVIE_LOADER_ID = 33377; // For Stage 2
    // Store json result data for youtube trailers and reviews of movies
    private String[] mVideoAndReviewHolder = new String[2];

    private ImageView mPosterImageView;
    private ImageView mTrailerIcon1;
    private ImageView mTrailerIcon2;
    private ImageView mTrailerIcon3;
    // For stage 2 - movie favorite
    private ImageView mFavoriteIcon;

    private TextView mMovieTitleTextView;
    private TextView mVoteAverageTextView;
    private TextView mSynopsisTextView;
    private TextView mReleaseDateTextView;
    private TextView mTrailer1;
    private TextView mTrailer2;
    private TextView mTrailer3;

    private String mVoteAverage;
    private String mTitle;
    private String mPosterImage;
    private String mSynopsis;
    private String mReleaseDate;
    private String mMovieId;

    // Create database member variable
    private AppDatabase mDb;

    // True if the movie is in DB, false if the movie is NOT in DB
    private boolean mFavoriteExistInDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterImageView = findViewById(R.id.poster_image_detail);
        mMovieTitleTextView = findViewById(R.id.title_detail);
        mVoteAverageTextView = findViewById(R.id.vote_average_detail);
        mSynopsisTextView = findViewById(R.id.synopsis_detail);
        mReleaseDateTextView = findViewById(R.id.release_date_detail);

        displayMovieInfo();

        // Initialize database member variable
        mDb = AppDatabase.getInstance(getApplicationContext());

        // For stage 2 - movie favorite
        mFavoriteIcon = findViewById(R.id.favorite_icon);

        // Using the movie id, add a movie info into DB or delete movie info from DB
        Intent intent = getIntent();
        mMovieId = intent.getStringExtra("bundleMovieId");
        if (!(mMovieId).equals("")) {
            FavoriteViewModelFactory factory = new FavoriteViewModelFactory(mDb, mMovieId);
            // Declare a FavoriteViewModel variable and initialize it by calling
            // ViewModelProviders.of for that use the factory created
            final FavoriteViewModel viewModel = ViewModelProviders.of(this, factory)
                    .get(FavoriteViewModel.class);

            // Observe the LiveData object in the ViewModel. Use it also when removing the observer
            viewModel.getFavoriteEntry().observe(this, new Observer<FavoriteEntry>() {
                @Override
                public void onChanged(@Nullable FavoriteEntry favoriteEntry) {
                    viewModel.getFavoriteEntry().removeObserver(this);
                    checkFavoriteData(favoriteEntry);
                    processFavoriteData();
                }
            });
        }

        // getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        android.support.v4.app.LoaderManager
                .getInstance(this).initLoader(MOVIE_LOADER_ID, null, this);
    }

    private void checkFavoriteData(FavoriteEntry favoriteEntry) {
        // Log.d(TAG, "Favorite Entry in Detail Activity: " + favoriteEntry);
        if (favoriteEntry == null) {
            mFavoriteExistInDB = false;
            return;
        }
        mFavoriteExistInDB = true;
    }

    private void processFavoriteData() {
        if (mFavoriteExistInDB) {
            // The star favorite image icon will be filled with yellow color
            mFavoriteIcon.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            // The star favorite image icon will be filled with grey color
            mFavoriteIcon.setImageResource(android.R.drawable.btn_star_big_off);
        }

        mFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveOrDeleteIconClicked();
            }
        });
    }

    private void onSaveOrDeleteIconClicked() {
        final FavoriteEntry favoriteEntry = new FavoriteEntry(mTitle,
                mSynopsis, mVoteAverage, mPosterImage, mReleaseDate, mMovieId);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mFavoriteExistInDB) {
                    mDb.favoriteDao().deleteFavoriteByMovieId(mMovieId);
                } else {
                    mDb.favoriteDao().insertFavorite(favoriteEntry);
                }
                finish();
            }
        });
    }

    private void displayMovieInfo() {
        mVoteAverage = getIntent().getStringExtra("bundleVoteAverage");
        mTitle = getIntent().getStringExtra("bundleMovieTitle");
        mPosterImage = getIntent().getStringExtra("bundlePosterImage");
        mSynopsis = getIntent().getStringExtra("bundleMovieSynopsis");
        mReleaseDate = getIntent().getStringExtra("bundleReleaseDate");

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + mPosterImage).into(mPosterImageView);
        mMovieTitleTextView.setText(mTitle);
        String getAverageOfTen = String.format("%s%s", mVoteAverage, getString(R.string.of_ten));
        mVoteAverageTextView.setText(getAverageOfTen);
        mSynopsisTextView.setText(mSynopsis);
        mReleaseDateTextView.setText(mReleaseDate);
    }

    // For Stage 2: Trailers, Reviews, and Favorites
    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<String[]>(this) {

            String mDataResult[] = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mDataResult != null) {
                    deliverResult(mDataResult);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String[] loadInBackground() {
                mMovieId = getIntent().getStringExtra("bundleMovieId");
                String videos = "videos";
                String reviews = "reviews";

                try {
                    // URLs for trailers and reviews
                    URL getVideoUrl = NetworkUtils.buildUrlWithMovieId(mMovieId, videos);
                    URL getReviewUrl = NetworkUtils.buildUrlWithMovieId(mMovieId, reviews);

                    // Log.d(TAG, "Video URL: " + getVideoUrl);
                    // Log.d(TAG, "Review URL: " + getReviewUrl);

                    // Get json responses from the URLs
                    String getVideoJsonResults = NetworkUtils.getResponseFromHttpUrl(getVideoUrl);
                    String getReviewJsonResults = NetworkUtils.getResponseFromHttpUrl(getReviewUrl);

                    // Store json results for videos
                    mVideoAndReviewHolder[0] = getVideoJsonResults;
                    // Store json results for reviews
                    mVideoAndReviewHolder[1] = getReviewJsonResults;

                    return mVideoAndReviewHolder;
                } catch (IOException e) {
                    Log.e(TAG, "IOException: " + e.getMessage());
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable String data[]) {
                super.deliverResult(data);
                mDataResult = data;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] jsonResults) {
        if (jsonResults != null) {
            // Log.d(TAG, "JSON Video Results: " + jsonResults[0]);
            // Log.d(TAG, "JSON Review Results: " + jsonResults[1]);
            try {
                boolean firstKey = false;
                boolean secondKey = false;
                boolean thirdKey = false;

                final List<String> getYoutubeKey = ParseJsonDataUtils.getMovieTrailersFromJson(jsonResults[0]);

                // Movie trailers can be displayed up to 3 trailers
                for (int i = 0; i < getYoutubeKey.size(); i++) {
                    // Log.d(TAG, "Youtube Key " + i + ": " + getYoutubeKey.get(i));
                    if (i == 0) {
                        firstKey = true;
                        mTrailer1 = findViewById(R.id.trailer1);
                        mTrailerIcon1 = findViewById(R.id.arrow1);
                    }

                    if (i == 1) {
                        secondKey = true;
                        mTrailer2 = findViewById(R.id.trailer2);
                        mTrailerIcon2 = findViewById(R.id.arrow2);
                    }

                    if (i == 2) {
                        thirdKey = true;
                        mTrailer3 = findViewById(R.id.trailer3);
                        mTrailerIcon3 = findViewById(R.id.arrow3);
                    }
                }

                if (firstKey) {
                    mTrailerIcon1.setVisibility(View.VISIBLE);
                    mTrailer1.setVisibility(View.VISIBLE);
                    mTrailer1.setOnClickListener(new View.OnClickListener() {
                        Uri movieTrailer1 = Uri
                                .parse("https://www.youtube.com/watch?v=" + getYoutubeKey.get(0));
                        Intent intentForTrailer1 = new Intent(Intent.ACTION_VIEW, movieTrailer1);
                        @Override
                        public void onClick(View v) {
                            if (intentForTrailer1.resolveActivity(getPackageManager()) != null) {
                                startActivity(intentForTrailer1);
                            }
                        }
                    });
                }

                if (secondKey) {
                    mTrailerIcon2.setVisibility(View.VISIBLE);
                    mTrailer2.setVisibility(View.VISIBLE);
                    mTrailer2.setOnClickListener(new View.OnClickListener() {
                        Uri movieTrailer2 = Uri
                                .parse("https://www.youtube.com/watch?v=" + getYoutubeKey.get(1));
                        Intent intentForTrailer2 = new Intent(Intent.ACTION_VIEW, movieTrailer2);
                        @Override
                        public void onClick(View v) {
                            startActivity(intentForTrailer2);
                        }
                    });
                }

                if (thirdKey) {
                    mTrailerIcon3.setVisibility(View.VISIBLE);
                    mTrailer3.setVisibility(View.VISIBLE);
                    mTrailer3.setOnClickListener(new View.OnClickListener() {
                        Uri movieTrailer3 = Uri
                                .parse("https://www.youtube.com/watch?v=" + getYoutubeKey.get(2));
                        Intent intentForTrailer3 = new Intent(Intent.ACTION_VIEW, movieTrailer3);
                        @Override
                        public void onClick(View v) {
                            startActivity(intentForTrailer3);
                        }
                    });
                }

                // No movie trailer key exists
                if (getYoutubeKey.size() == 0) {
                    mTrailer1 = findViewById(R.id.trailer1);
                    mTrailer1.setVisibility(View.VISIBLE);
                    mTrailer1.setTextColor(R.color.colorAccent);
                    mTrailer1.setText(R.string.no_trailer_exist);
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON Exception for Youtube Key: " + e.getMessage());
            }

            try {
                final URL getReviewUrl = ParseJsonDataUtils.getMovieReviewsFromJson(jsonResults[1]);

                // The url for a movie url is null
                if (getReviewUrl == null) {
                    ImageView mMovieReviewIcon = findViewById(R.id.movie_review_icon);
                    mMovieReviewIcon.setVisibility(View.GONE);

                    TextView mMovieReview = findViewById(R.id.movie_review);
                    mMovieReview.setText(R.string.no_movie_reviews);
                } else {
                    //Log.d(TAG, "Movie Review URL: " + getReviewUrl);
                    TextView mMovieReview = findViewById(R.id.movie_review);
                    mMovieReview.setOnClickListener(new View.OnClickListener() {
                        Uri parsedUrl = Uri.parse(getReviewUrl.toString());
                        Intent movieReviewIntent = new Intent(Intent.ACTION_VIEW, parsedUrl);

                        @Override
                        public void onClick(View v) {
                            startActivity(movieReviewIntent);
                        }
                    });
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSONException for Movie Review: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "The json results of movie and reviews are NULL");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }
}