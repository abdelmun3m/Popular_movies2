package com.abdelmun3m.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import com.abdelmun3m.popular_movies.MoviewRecyclerView.*;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClick ,View.OnClickListener{

    RecyclerView mRecyclerView ;
    MoviesAdapter mAdapter;
    TextView errorTextView;
    ProgressBar mLoadingProgres;
    TextView popular,top_sorted;
    AsyncTask<URL, Void, List<Movie>> load ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_moviesView);
        errorTextView = (TextView) findViewById(R.id.tv_ErrorMessage);
        mLoadingProgres=(ProgressBar) findViewById(R.id.pb_loading);
        popular = (TextView) findViewById(R.id.tv_popular);
        top_sorted = (TextView) findViewById(R.id.tv_top_sorted);
        popular.setOnClickListener(this);
        top_sorted.setOnClickListener(this);
        LinearLayoutManager manger = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(manger);
        mRecyclerView.setHasFixedSize(true);

        LoadMovies(1);
    }

    private void LoadMovies(int sortType) {

        if(NetworkConnectivityAvilable()){
            changeTextColors(sortType);
            showMoviesList();
            mAdapter = new MoviesAdapter(this);
            mRecyclerView.setAdapter(mAdapter);
            URL murl= Movie_API.Build_Sorted_Movies_URL(sortType);
            if(load == null){
                load =  new MoviesLoadtask().execute(murl);
            }
        }else{
            Toast.makeText(this, "no network", Toast.LENGTH_SHORT).show();
            showErrorMessage();
        }
    }


    public void changeTextColors(int i){
        if(i == 1){
            top_sorted.setTextColor(Color.parseColor("#01d277"));
            popular.setTextColor(Color.parseColor("#081c24"));
        }
        else if(i==2){
            popular.setTextColor(Color.parseColor("#01d277"));
            top_sorted.setTextColor(Color.parseColor("#081c24"));
        }
    }


    @Override
    public void onClick(View v) {
        int item = v.getId();
        if(item == popular.getId()){
            if(load!=null){
                load.cancel(true);
                load = null;
            }

            LoadMovies(2);

        }else if(item == top_sorted.getId()){
            if(load!=null){
                load.cancel(true);
                load = null;
            }


            LoadMovies(1);
        }
    }

    private void showMoviesList() {
        mRecyclerView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
    }



    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieClick(Movie m) {
        if(m != null){
            Intent in = new Intent(MainActivity.this,MovieDetails.class);
            in.putExtra(General_Data.INTENT_TAG,m);
            startActivity(in);
        }
    }


    public boolean NetworkConnectivityAvilable(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    /**
     *
     * movies Load
     * **/
    public class MoviesLoadtask extends AsyncTask<URL,Void,List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showMoviesList();
            mLoadingProgres.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(URL... params) {

           String response = Movie_API.get_Response(params[0]);

            if(response == null || response.equals("")){
                return null;
            }

            try {
               return Movie_API.getListOfMovies(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mLoadingProgres.setVisibility(View.INVISIBLE);
            if(movies != null){
                mAdapter.UpdateListOfMovies(movies);
                showMoviesList();
            }else {
                showErrorMessage();
            }
        }
    }


}
