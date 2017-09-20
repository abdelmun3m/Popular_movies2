package com.abdelmun3m.popular_movies;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by abdelmun3m on 18/09/17.
 */

public class Movie_API {


    private static final String TAG = General_Data.TAG+":"+Movie_API.class.getSimpleName();




    public static URL Build_Sorted_Movies_URL(int SortType){

            /**
             * return the URL of the movies according sort type
             * if 1 --> return Top Rated sort movies url
             * if 2---> return pupular sort movies url
             * else return null;
             *
             *
             * general URL form
             * https://api.themoviedb.org/3/movie/(popular/topRated)?api_key=<<api_key>>&language=en-US&page=1
             * **/

        String sort_URI;
        if(SortType == 1){sort_URI = General_Data.TOP_RATED_MOVIES_URL;}
        else if(SortType == 2){sort_URI = General_Data.POPULAR_MOVIES_URL;}
        else{sort_URI = null;}
        Uri uri = Uri.parse(sort_URI).buildUpon()
                .appendQueryParameter(General_Data.QUERY_API_KEY,General_Data.API_KEY)
                .appendQueryParameter(General_Data.QUERY_LANGUAGE,General_Data.DEFAULT_LANG)
                .appendQueryParameter(General_Data.QUERY_PAGE,String.valueOf(General_Data.PAGE_NUMBER))
                .build();

        URL url = null;
        try {
             url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"Builed URL : "+ url.toString());
        return url;
    }


    /**
     * start the connection use the URL generated ,
     * get input stream ,
     * convert it to string and return a json string of the response
     *  if there is no response the return null
     * **/
    public static String get_Response(URL url){
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            InputStream response = connection.getInputStream();
            Scanner scan= new Scanner(response);
            scan.useDelimiter("\\A");

            if(scan.hasNext()){
                return scan.next();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return null;
    }


    public static List<Movie> getListOfMovies(String Result) throws JSONException{

        List<Movie> ListOfMovies = new ArrayList<>();
        JSONObject jsonResult= new JSONObject(Result);

        JSONArray results = jsonResult.getJSONArray(General_Data.Json_Result);
        for (int i = 0; i <results.length() ; i++) {
            JSONObject JsonMovie = results.getJSONObject(i);
            Movie temp = new Movie();
            temp.Movie_Id = JsonMovie.getInt(General_Data.Json_MOVIE_id);
            temp.PosterImage = JsonMovie.getString(General_Data.Json_MOVIE_POSTER_IMAGE);
            temp.OriginallTitle = JsonMovie.getString(General_Data.Json_MOVIE_ORIGINAL_TITLE);
            temp.Overview = JsonMovie.getString(General_Data.Json_MOVIE_OVERVIEW);
            temp.RelaseDate = JsonMovie.getString(General_Data.Json_MOVIE_RELEASE_DATE);
            temp.Vote_Average = JsonMovie.getLong(General_Data.Json_MOVIE_VOTE_AVERAGE);
            ListOfMovies.add(temp);
        }
        return ListOfMovies;
    }

}
