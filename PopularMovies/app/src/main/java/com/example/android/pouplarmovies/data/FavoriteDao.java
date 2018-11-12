package com.example.android.pouplarmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    LiveData<List<FavoriteEntry>> loadAllFavorites();

    @Insert
    void insertFavorite(FavoriteEntry favoriteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(FavoriteEntry favoriteEntry);

    @Delete
    void deleteFavorite(FavoriteEntry favoriteEntry);

    @Query("DELETE FROM favorites WHERE movie_id = :movieIdForDeletion")
    void deleteFavoriteByMovieId(String movieIdForDeletion);

    @Query("SELECT * FROM favorites WHERE id = :id")
    LiveData<FavoriteEntry> loadFavoriteById(int id);

    @Query("SELECT * FROM favorites WHERE movie_id = :movieId")
    LiveData<FavoriteEntry> loadFavoriteByMovieId(String movieId);
}
