package com.abdelmun3m.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class MovieAPI {

    private static final String TAG = GeneralData.TAG+":"+MovieAPI.class.getSimpleName();

    private static final String VIDEOS_PATH = "videos";
    private static final String REVIEWS_PATH = "reviews";
    private static final String MOVIE_PATH = "movie";



    public static URL Build_Sorted_Movies_URL(int SortType){
        String sort_URI;
        if(SortType == 1){sort_URI = GeneralData.TOP_RATED_MOVIES_URL;}
        else if(SortType == 2){sort_URI = GeneralData.POPULAR_MOVIES_URL;}
        else{sort_URI = null;}
        Uri uri = Uri.parse(sort_URI).buildUpon()
                .appendQueryParameter(GeneralData.QUERY_API_KEY, GeneralData.API_KEY)
                .appendQueryParameter(GeneralData.QUERY_LANGUAGE, GeneralData.DEFAULT_LANG)
                .appendQueryParameter(GeneralData.QUERY_PAGE,GeneralData.PAGE_NUMBER)
                .build();

        URL url = null;
        try {
             url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"Builed URL : "+ (url != null ? url.toString() : null));
        return url;
    }

    public static URL Build_Movie_Trailer_Url(String movieID){

        Uri uri = Uri.parse(GeneralData.MOVIE_API_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(movieID).appendPath(VIDEOS_PATH)
                .appendQueryParameter(GeneralData.QUERY_API_KEY,GeneralData.API_KEY)
                .appendQueryParameter(GeneralData.QUERY_LANGUAGE, GeneralData.DEFAULT_LANG)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL Build_Movies_Review_Url(String movieId){
        Uri uri = Uri.parse(GeneralData.MOVIE_API_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(movieId).appendPath(REVIEWS_PATH)
                .appendQueryParameter(GeneralData.QUERY_API_KEY,GeneralData.API_KEY)
                .appendQueryParameter(GeneralData.QUERY_LANGUAGE, GeneralData.DEFAULT_LANG)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"Trailer URL : "+ (url != null ? url.toString() : null));
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
            assert connection != null;
            connection.disconnect();
        }
        return null;
    }

    public static List<Movie> getListOfMovies(String Result) throws JSONException{

        List<Movie> ListOfMovies = new ArrayList<>();
        JSONObject jsonResult= new JSONObject(Result);

        JSONArray results = jsonResult.getJSONArray(GeneralData.Json_Result);
        for (int i = 0; i <results.length() ; i++) {
            JSONObject JsonMovie = results.getJSONObject(i);
            Movie temp = new Movie();
            temp.Movie_Id = JsonMovie.getString(GeneralData.Json_MOVIE_id);
            temp.PosterImage = JsonMovie.getString(GeneralData.Json_MOVIE_POSTER_IMAGE);
            temp.OriginallTitle = JsonMovie.getString(GeneralData.Json_MOVIE_ORIGINAL_TITLE);
            temp.Overview = JsonMovie.getString(GeneralData.Json_MOVIE_OVERVIEW);
            temp.RelaseDate = JsonMovie.getString(GeneralData.Json_MOVIE_RELEASE_DATE);
            temp.Vote_Average = JsonMovie.getLong(GeneralData.Json_MOVIE_VOTE_AVERAGE);
            ListOfMovies.add(temp);
        }
        return ListOfMovies;
    }

    public static String[] getTrailerKey(String result) throws  JSONException{
        JSONObject obj = new JSONObject(result);
        JSONArray jsonResult =obj.getJSONArray("results");
        String[] kes = new String[jsonResult.length()];
        for(int i = 0 ; i < jsonResult.length();i++){
            JSONObject trailer = jsonResult.getJSONObject(i);
            if(trailer.has(GeneralData.Json_MOVIE_TRAILER_KEY)){
                kes[i] = trailer.getString(GeneralData.Json_MOVIE_TRAILER_KEY);
            }
        }
        return kes;
    }

    public static final String REVIEW_URL = "url";
    public static final String REVIEW_AUTHOR = "author";

    public static List<Review> getReviewURL(String result) throws  JSONException{

        List<Review> reviews = new ArrayList<>();
        JSONObject obj = new JSONObject(result);
        JSONArray results = obj.getJSONArray("results");
        for (int i = 0 ; i <results.length(); i++){
           Review review = new Review();
            JSONObject Jsonreview = results.getJSONObject(i);
            if(Jsonreview.has(REVIEW_URL)){
                review.url = Jsonreview.getString(REVIEW_URL);
                GeneralData.mylog(review.url);
            }if(Jsonreview.has(REVIEW_AUTHOR)){
                review.author = Jsonreview.getString(REVIEW_AUTHOR);
            }
            reviews.add(review);
        }
        return reviews;
    }


    public static boolean NetworkConnectivityAvilable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

}
