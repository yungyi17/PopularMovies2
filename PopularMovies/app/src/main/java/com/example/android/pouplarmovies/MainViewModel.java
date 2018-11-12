package com.example.android.pouplarmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.pouplarmovies.data.AppDatabase;
import com.example.android.pouplarmovies.data.FavoriteEntry;

import java.util.List;

// Create MainViewModel and use this ViewModel to cache the list of favorite entry objects
// wrap in a live data object. This variable will be private and will have a public getter.
public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    // Private variable for live data object
    private LiveData<List<FavoriteEntry>> favoriteEntries;

    public MainViewModel(Application application) {
        super(application);
        // Use the loadAllFavorites of the favoriteDao to initialize the favorites variable
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        favoriteEntries = database.favoriteDao().loadAllFavorites();
    }

    // A public getter for the favorites variable
    public LiveData<List<FavoriteEntry>> getFavoriteEntries() {
        return favoriteEntries;
    }
}
