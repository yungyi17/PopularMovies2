package com.example.android.pouplarmovies.data;

import java.util.List;

public class SaveMovieData {

    private static List<String> voteAverage;
    private static List<String> movieTitle;
    private static List<String> posterImage;
    private static List<String> movieSynopsis;
    private static List<String> releaseDate;
    // For Stage 2: Trailers, Reviews, and Favorites
    private static List<String> movieId;

    public static void setVoteAverage(List<String> vAverage) {
        voteAverage = vAverage;
    }

    public static void setMovieTitle(List<String> mTitle) {
        movieTitle = mTitle;
    }

    public static void setPosterImage(List<String> pImage) {
        posterImage = pImage;
    }

    public static void setMovieSynopsis(List<String> mSynopsis) {
        movieSynopsis = mSynopsis;
    }

    public static void setReleaseDate(List<String> rDate) {
        releaseDate = rDate;
    }

    // For Stage 2: Trailers, Reviews, and Favorites
    public static void setMovieId(List<String> mId) {
        movieId = mId;
    }

    public static List<String> getVoteAverage() {
        return voteAverage;
    }

    public static List<String> getMovieTitle() {
        return movieTitle;
    }

    public static List<String> getPosterImage() {
        return posterImage;
    }

    public static List<String> getMovieSynopsis() {
        return movieSynopsis;
    }

    public static List<String> getReleaseDate() {
        return releaseDate;
    }

    // For Stage 2: Trailers, Reviews, and Favorites
    public static List<String> getMovieId() {
        return movieId;
    }
}
