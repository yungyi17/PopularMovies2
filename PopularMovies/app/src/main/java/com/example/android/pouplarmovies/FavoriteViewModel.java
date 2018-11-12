package com.example.android.pouplarmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.pouplarmovies.data.AppDatabase;
import com.example.android.pouplarmovies.data.FavoriteEntry;

public class FavoriteViewModel extends ViewModel {
    private LiveData<FavoriteEntry> favoriteEntry;

    public FavoriteViewModel(AppDatabase database, String movieId) {
        favoriteEntry = database.favoriteDao().loadFavoriteByMovieId(movieId);
    }

    public LiveData<FavoriteEntry> getFavoriteEntry() {
        return favoriteEntry;
    }
}
