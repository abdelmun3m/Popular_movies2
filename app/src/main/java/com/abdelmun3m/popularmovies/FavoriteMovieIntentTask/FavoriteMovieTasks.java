package com.abdelmun3m.popularmovies.FavoriteMovieIntentTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.abdelmun3m.popularmovies.GeneralData;
import com.abdelmun3m.popularmovies.Movie;
import com.abdelmun3m.popularmovies.MoviesProvider.MoviesContentProvider;
import com.abdelmun3m.popularmovies.MoviesProvider.MoviesContract;

/**
 * Created by abdelmun3m on 11/10/17.
 */

public class FavoriteMovieTasks {


    public static final String INSERT_NEW_FAVORITE_MOVIE_TASK = "insert";
    public static final String DELETE_FAVORITE_MOVIE_TASK = "delete";
    public static final String MOVIE_INTENT_KEY = "movie_key";
    public static final String DELETE_MOVIE_INTENT_KEY = "delete_id";

   public static final MoviesContentProvider mprovider = new MoviesContentProvider();

    public static void deleteMovie(Context context, String id) {
        Uri uri = MoviesContract.FavoriteMoviesEntity.BuildMovieUriWithId(id);
        int delete = context.getContentResolver().delete(uri, null, null);
        GeneralData.mylog("Deleted : "+delete + " : "+uri.toString());
    }
    public static void insertMovie(Context context,Movie movie) {
        Uri insertedUri = movieToContentValue(context,movie);
        GeneralData.mylog("Inserted : "+insertedUri.toString());
    }


    public static Uri movieToContentValue(Context context, Movie movie){
        Uri uri = MoviesContract.FavoriteMoviesEntity.FAVORITE_MOVIES_URI;
        ContentValues value = new ContentValues();
        value.put(MoviesContract.FavoriteMoviesEntity.COLUMN_MOVIE_ID,movie.Movie_Id);
        value.put(MoviesContract.FavoriteMoviesEntity.COLUMN_ORIGINAL_TITLE,movie.OriginallTitle);
        value.put(MoviesContract.FavoriteMoviesEntity.COLUMN_OVERVIEW,movie.Overview);
        value.put(MoviesContract.FavoriteMoviesEntity.COLUMN_POSTER_IMAGE,movie.PosterImage);
        value.put(MoviesContract.FavoriteMoviesEntity.COLUMN_RELEASE_DATE,movie.RelaseDate);
        value.put(MoviesContract.FavoriteMoviesEntity.COLUMN_VOTE_RATE,movie.Vote_Average);
        return context.getContentResolver().insert(uri,value);
    }


}
