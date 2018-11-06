package com.example.android.pouplarmovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String overview;
    @ColumnInfo(name = "voter_average")
    private String voterAverage;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "movie_id")
    private String movieId;

    @Ignore
    public FavoriteEntry(String title, String overview, String voterAverage,
                         String posterPath, String releaseDate, String movieId) {
        this.title = title;
        this.overview = overview;
        this.voterAverage = voterAverage;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }

    public FavoriteEntry(int id, String title, String overview, String voterAverage,
                         String posterPath, String releaseDate, String movieId) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.voterAverage = voterAverage;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setVoterAverage(String voterAverage) {
        this.voterAverage = voterAverage;
    }

    public String getVoterAverage() {
        return voterAverage;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }
}
