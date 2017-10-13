package com.abdelmun3m.popularmovies;


import android.util.Log;

public class GeneralData {

    public static final String API_KEY = "a26b061467611fb1fc2dabf560a402c6";
    public static final String MOVIE_API_URL  = "https://api.themoviedb.org/3/";
    public static final String GENERAL_MOVIES_URL  = MOVIE_API_URL+"movie/";
    public static final String POPULAR_MOVIES_URL  = GENERAL_MOVIES_URL+"popular";
    public static final String TOP_RATED_MOVIES_URL   = GENERAL_MOVIES_URL+"top_rated";
    public static final String QUERY_API_KEY = "api_key";
    public static final String QUERY_LANGUAGE = "language";
    public static final String DEFAULT_LANG   = "en-US";
    public static final String QUERY_PAGE  = "page";
    public static String PAGE_NUMBER = "1,2";
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    public static final String  TAG= "PouplarMovies";
    public static final String Json_Result  = "results";
    public static final String Json_MOVIE_id  = "id";
    public static final String Json_MOVIE_OVERVIEW  = "overview";
    public static final String Json_MOVIE_ORIGINAL_TITLE  = "original_title";
    public static final String Json_MOVIE_RELEASE_DATE  = "release_date";
    public static final String Json_MOVIE_POSTER_IMAGE  = "poster_path";
    public static final String Json_MOVIE_VOTE_AVERAGE  = "vote_average";
    public static final String Json_MOVIE_TRAILER_KEY  = "key";
    public static final String INTENT_TAG = "Selected_Movie";
    public static final String YOUTUBE_MOVIE = "https://www.youtube.com/watch?v=";


    public static void mylog(String s){
        Log.w(TAG,s);
    }

}
