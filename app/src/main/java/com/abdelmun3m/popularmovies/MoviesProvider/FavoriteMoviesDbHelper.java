package com.abdelmun3m.popularmovies.MoviesProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abdelmun3m on 05/10/17.
 */

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {



    public static final String MOVIE_DB_NAME ="MOVIES_DB";
    public static final int VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, MOVIE_DB_NAME,null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_FAVORITE_MOVIE_TABLE ="CREATE TABLE "
                + MoviesContract.FavoriteMoviesEntity.TABLE_NAME
                +"("
                + MoviesContract.FavoriteMoviesEntity._ID + " INTEGER PRIMARY KEY Autoincrement"+","
                + MoviesContract.FavoriteMoviesEntity.COLUMN_MOVIE_ID + " TEXT NOT NULL unique"+","
                + MoviesContract.FavoriteMoviesEntity.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL"+","
                + MoviesContract.FavoriteMoviesEntity.COLUMN_POSTER_IMAGE + " TEXT NOT NULL"+","
                + MoviesContract.FavoriteMoviesEntity.COLUMN_OVERVIEW+ " TEXT NOT NULL"+","
                + MoviesContract.FavoriteMoviesEntity.COLUMN_RELEASE_DATE+ " TEXT NOT NULL"+","
                + MoviesContract.FavoriteMoviesEntity.COLUMN_VOTE_RATE+" REAL NOT NULL"
               // + " UNIQUE (" + MoviesContract.FavoriteMoviesEntity.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE"
                +");";

        db.execSQL(CREATE_FAVORITE_MOVIE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_FAVORITE_MOVIES_TABLE = "DROP TABLE IF EXISTS "+ MoviesContract.FavoriteMoviesEntity.TABLE_NAME;
        db.execSQL(DROP_FAVORITE_MOVIES_TABLE);
        onCreate(db);
    }
}
