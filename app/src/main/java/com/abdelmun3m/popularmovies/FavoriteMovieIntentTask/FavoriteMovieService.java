package com.abdelmun3m.popularmovies.FavoriteMovieIntentTask;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.abdelmun3m.popularmovies.Movie;

/**
 * Created by abdelmun3m on 11/10/17.
 */

public class FavoriteMovieService extends IntentService {

    public FavoriteMovieService() {
        super("FavoriteMovieService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        switch (action){
            case FavoriteMovieTasks.INSERT_NEW_FAVORITE_MOVIE_TASK:
                Movie movie = intent.getParcelableExtra(FavoriteMovieTasks.MOVIE_INTENT_KEY);
                FavoriteMovieTasks.insertMovie(getApplicationContext(),movie);break;
            case FavoriteMovieTasks.DELETE_FAVORITE_MOVIE_TASK:
                String id = intent.getStringExtra(FavoriteMovieTasks.DELETE_MOVIE_INTENT_KEY);
                FavoriteMovieTasks.deleteMovie(getApplicationContext(),id);
        }
    }

}
