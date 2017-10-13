package com.abdelmun3m.popularmovies.MoviesProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.abdelmun3m.popularmovies.GeneralData;
import com.abdelmun3m.popularmovies.Movie;

/**
 * Created by abdelmun3m on 05/10/17.
 */

public class MoviesContentProvider extends ContentProvider{

    public static final int FAVOIRTE_MOVIES_URL_KEY = 111;
    public static final int FAVOIRTE_MOVIES_WITH_ID_KEY = 112;


    FavoriteMoviesDbHelper mhelper ;



    static UriMatcher mUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MoviesContract.AUTHORITE, MoviesContract.PATH_FAVORITE_MOVIE, FAVOIRTE_MOVIES_URL_KEY);
        matcher.addURI(MoviesContract.AUTHORITE, MoviesContract.PATH_FAVORITE_MOVIE+"/#", FAVOIRTE_MOVIES_WITH_ID_KEY);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mhelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
       SQLiteDatabase db = mhelper.getReadableDatabase();
        int matcher = mUriMatcher.match(uri);
         Cursor queryResult = null;
        switch (matcher){
            case FAVOIRTE_MOVIES_URL_KEY:
                queryResult = db.query(MoviesContract.FavoriteMoviesEntity.TABLE_NAME
                ,projection,selection,selectionArgs,null,null, MoviesContract.FavoriteMoviesEntity._ID);
                break;
            case FAVOIRTE_MOVIES_WITH_ID_KEY:
                String id = uri.getLastPathSegment();
                selection = MoviesContract.FavoriteMoviesEntity.COLUMN_MOVIE_ID+"=?";
                selectionArgs = new String[]{id};
                queryResult = db.query(MoviesContract.FavoriteMoviesEntity.TABLE_NAME
                        ,projection,selection,selectionArgs,null,null, MoviesContract.FavoriteMoviesEntity._ID);
                break;
            default: throw  new android.database.SQLException("Unknown Uri : "+ uri);
        }
        queryResult.setNotificationUri(getContext().getContentResolver(),uri);
        return queryResult;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        SQLiteDatabase db = mhelper.getWritableDatabase();
        int matcher = mUriMatcher.match(uri);
        Uri insertURI = null;
        switch (matcher){
            case FAVOIRTE_MOVIES_URL_KEY :
                long insertID = db.insert(MoviesContract.FavoriteMoviesEntity.TABLE_NAME,null,values);
                if(insertID > 0){
                    insertURI =
                            ContentUris.withAppendedId(MoviesContract.FavoriteMoviesEntity.FAVORITE_MOVIES_URI,insertID);
                }else {
                    throw new android.database.SQLException("Failed to insert Data Id : " + insertID);
                }
                break;
            default: throw new android.database.SQLException("Unknown URI : "+ uri);

        }

        getContext().getContentResolver().notifyChange(insertURI,null);

        return insertURI;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mhelper.getWritableDatabase();
        int matcher = mUriMatcher.match(uri);
        int delededResult = 0;
        String id = uri.getLastPathSegment();
        switch (matcher){
            case FAVOIRTE_MOVIES_WITH_ID_KEY:
                delededResult = db.delete(MoviesContract.FavoriteMoviesEntity.TABLE_NAME,
                        MoviesContract.FavoriteMoviesEntity.COLUMN_MOVIE_ID + "=?",
                        new String[]{id});
                break;
            default:new android.database.SQLException("UnKnown Uri : "+uri);
        }
        if(delededResult != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }else {

            throw new android.database.SQLException("Con not delete Uri with Id : "+id);
        }

        return delededResult;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mhelper.getWritableDatabase();
        int matcher = mUriMatcher.match(uri);
        int updateResult = 0;
        String id = uri.getPathSegments().get(0);
        switch (matcher){
            case FAVOIRTE_MOVIES_WITH_ID_KEY:
                int updatedId = db.update(MoviesContract.FavoriteMoviesEntity.TABLE_NAME,
                        values,
                        MoviesContract.FavoriteMoviesEntity._ID+"=?"
                        ,new String[]{id});
                break;
            default:throw new android.database.SQLException("Unknown URI With ID : "+id);
        }
        if(updateResult !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return updateResult;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mhelper.getWritableDatabase();

        int matcher = mUriMatcher.match(uri);

        switch (matcher){

            case 111:
                db.beginTransaction();
                int insertedRows = 0;
                try {

                    for (ContentValues value : values
                         ) {

                        long id = db.insert(MoviesContract.FavoriteMoviesEntity.TABLE_NAME,null,value);
                        if(id != -1){
                            insertedRows++;
                        }
                    }
                    db.setTransactionSuccessful();

                }finally {
                    db.endTransaction();
                }
                if(insertedRows > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return insertedRows;
            default:return super.bulkInsert(uri, values);
        }
    }


}
