package com.abdelmun3m.popularmovies.MoviesProvider;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;

/**
 * Created by abdelmun3m on 05/10/17.
 */

public class MoviesContract {

     private MoviesContract(){}



    public static final String AUTHORITE = "com.abdelmun3m.popularmovies";
    public static final Uri BASE_MOVIES_URI = Uri.parse("content://"+AUTHORITE);
    public static final String PATH_FAVORITE_MOVIE = "FavoriteMovies";


    public static class FavoriteMoviesEntity implements BaseColumns {

        public static Uri FAVORITE_MOVIES_URI = BASE_MOVIES_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIE).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MOVIE_ID ="movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_IMAGE="image";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_RELEASE_DATE = "data";
        public static final String COLUMN_VOTE_RATE = "vote";

        public static final int  INDEX_COLUMN_MOVIE_DB_ID =0;
        public static final int  INDEX_COLUMN_MOVIE_ID =1;

        public static final int INDEX_COLUMN_ORIGINAL_TITLE = 2;
        public static final int INDEX_COLUMN_POSTER_IMAGE=3;
        public static final int INDEX_COLUMN_OVERVIEW=4;
        public static final int INDEX_COLUMN_RELEASE_DATE = 5;
        public static final int INDEX_COLUMN_VOTE_RATE = 6;



        public static Uri BuildMovieUriWithId(String id){

            return FAVORITE_MOVIES_URI.buildUpon().appendPath(id).build();

        }
    }



}
