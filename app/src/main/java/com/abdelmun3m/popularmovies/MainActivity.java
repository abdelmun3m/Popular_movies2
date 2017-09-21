package com.abdelmun3m.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import com.abdelmun3m.popularmovies.MoviewRecyclerView.*;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClick ,View.OnClickListener{

    private RecyclerView mRecyclerView ;
   private MoviesAdapter mAdapter;
  private   TextView errorTextView;
    private  ProgressBar mLoadingProgres;
    private TextView popular,topRated;
    private AsyncTask<URL, Void, List<Movie>> load ;
    private static final int  PopularListSelection = 2;
    private static final int  TopRatedListSelection = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_moviesView);
        errorTextView = (TextView) findViewById(R.id.tv_ErrorMessage);
        mLoadingProgres=(ProgressBar) findViewById(R.id.pb_loading);
        popular = (TextView) findViewById(R.id.tv_popular);
        topRated = (TextView) findViewById(R.id.tv_top_rated);
        popular.setOnClickListener(this);
        topRated.setOnClickListener(this);
        GridLayoutManager manager =new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        LoadMovies(TopRatedListSelection);
    }

    private void LoadMovies(int sortType) {

        if(NetworkConnectivityAvilable()){
            changeTextColors(sortType);
            showMoviesList();
            mAdapter = new MoviesAdapter(this);
            mRecyclerView.setAdapter(mAdapter);
            URL murl= MovieAPI.Build_Sorted_Movies_URL(sortType);
            if(load == null){
                load =  new MoviesLoadtask().execute(murl);
            }
        }else{
            Toast.makeText(this,""+getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            showErrorMessage();
        }
    }


    private void changeTextColors(int i){
        if(i == TopRatedListSelection){
            topRated.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
            popular.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
        else if(i==PopularListSelection){
            popular.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
            topRated.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
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

            LoadMovies(PopularListSelection);

        }else if(item == topRated.getId()){
            if(load!=null){
                load.cancel(true);
                load = null;
            }


            LoadMovies(TopRatedListSelection);
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
            in.putExtra(GeneralData.INTENT_TAG,m);
            startActivity(in);
        }
    }


    private boolean NetworkConnectivityAvilable(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    /**
     *
     * movies Load
     *
     *
     * **/

    private class MoviesLoadtask extends AsyncTask<URL,Void,List<Movie>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showMoviesList();
            mLoadingProgres.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(URL... params) {

            String response = MovieAPI.get_Response(params[0]);

            if(response == null || response.equals("")){
                return null;
            }

            try {
                return MovieAPI.getListOfMovies(response);
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
