package com.abdelmun3m.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
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

import com.abdelmun3m.popularmovies.MoviesProvider.MoviesContract;
import com.abdelmun3m.popularmovies.MoviewRecyclerView.*;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClick ,
        View.OnClickListener ,
        LoaderManager.LoaderCallbacks<List<Movie>>{

   @BindView(R.id.rv_moviesView) RecyclerView mRecyclerView ;
   @BindView(R.id.tv_ErrorMessage) TextView errorTextView;
   @BindView(R.id.pb_loading) ProgressBar mLoadingProgres;
   @BindView(R.id.tv_popular) TextView popular;
   @BindView(R.id.tv_top_rated) TextView topRated;
   @BindView(R.id.tv_favorit_movies) TextView favorite;
   @BindView(R.id.search) android.widget.SearchView searchView ;

    private MoviesAdapter mAdapter;
    private AsyncTask<URL, Void, List<Movie>> load ;
    private static final int POPULAR_LIST_SELECTION = 2;
    private static final int TOP_RATED_LIST_SELECTION = 1;
    private static final int FAVORITE_MOVIES_SELECTION = 3;
    private static final int ActivityLoaderID = 100;
    private int currentSortSelection = TOP_RATED_LIST_SELECTION;
    private static final String SORT_STORE_KEY = "SortKey";
    private static int SCREEN_SIZE = 1;
    private static final  int ERROR_EMPTY_FAVORITE_MOVIES = 1;
    private static final int ERROR_NO_CONNECTION_ = 2;
    private static final int ERROR_LOADING_ERROR = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        popular.setOnClickListener(this);
        topRated.setOnClickListener(this);
        favorite.setOnClickListener(this);
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(SORT_STORE_KEY)){
                currentSortSelection = savedInstanceState.getInt(SORT_STORE_KEY);
            }
        }
        setLayoutManager();
        loadMovies(currentSortSelection,false);
    }

    private void setLayoutManager(){
        GridLayoutManager manager =new GridLayoutManager(this, SCREEN_SIZE,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GeneralData.mylog("onSaveInstanceState");
        outState.putInt(SORT_STORE_KEY, currentSortSelection);
    }

    private void ChangeIndicatorVisibility(boolean visible){
        if(visible){
            mLoadingProgres.setVisibility(View.VISIBLE);
        }else {
            mLoadingProgres.setVisibility(View.INVISIBLE);
        }
    }
    private void loadMovies(int sortType, boolean restart) {
        if(MovieAPI.NetworkConnectivityAvilable(MainActivity.this)){
            changeTextColors(sortType);
            showMoviesList();
            mAdapter = new MoviesAdapter(this);
            mRecyclerView.setAdapter(mAdapter);
            currentSortSelection = sortType;
            if(!restart){
                GeneralData.mylog("intiaLoader");
                getSupportLoaderManager().initLoader(ActivityLoaderID,null,this);

            }else {
                GeneralData.mylog("restartLoader");
                getSupportLoaderManager().restartLoader(ActivityLoaderID,null,this);
            }
        }else{
            showErrorMessage(ERROR_NO_CONNECTION_);
        }
    }


    public void loadDbMovies(){
        changeTextColors(FAVORITE_MOVIES_SELECTION);
        showMoviesList();
        Cursor query = getContentResolver()
                .query(MoviesContract.FavoriteMoviesEntity.FAVORITE_MOVIES_URI,null,null,null,null,null);
        if(query.getCount() > 0){
            mAdapter = new MoviesAdapter(this);
            mAdapter.convertCursorToMovies(query);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            showErrorMessage(ERROR_EMPTY_FAVORITE_MOVIES);
        }
        currentSortSelection = FAVORITE_MOVIES_SELECTION;
    }

    private void changeTextColors(int i){
        if(i == TOP_RATED_LIST_SELECTION){
            topRated.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
            popular.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            favorite.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
        else if(i== POPULAR_LIST_SELECTION){
            popular.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
            topRated.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            favorite.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }else if(i== FAVORITE_MOVIES_SELECTION){
            favorite.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
            popular.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            topRated.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentSortSelection == FAVORITE_MOVIES_SELECTION){
            loadDbMovies();
        }
    }

    @Override
    public void onClick(View v) {
        int item = v.getId();
        if(item == popular.getId()){

            loadMovies(POPULAR_LIST_SELECTION,true);

        }else if(item == topRated.getId()){

            loadMovies(TOP_RATED_LIST_SELECTION,true);
        }else if(item == favorite.getId())
        {
            loadDbMovies();
        }
    }

    private void showMoviesList() {
        mRecyclerView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
    }


    private void showErrorMessage(int error) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        switch (error){
            case ERROR_EMPTY_FAVORITE_MOVIES:errorTextView.setText(getString(R.string.favorit_error));break;
            case ERROR_LOADING_ERROR : errorTextView.setText(getString(R.string.error_Message));break;
            case ERROR_NO_CONNECTION_: errorTextView.setText(getString(R.string.network_error));break;
            default:errorTextView.setText(getString(R.string.error_Message));
        }

    }

    @Override
    public void onMovieClick(Movie m) {
        if(m != null){
            Intent in = new Intent(MainActivity.this,MovieDetails.class);
            in.putExtra(GeneralData.INTENT_TAG,m);
            startActivity(in);
        }
    }
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> cashMovies;
            @Override
            protected void onStartLoading() {
                showMoviesList();
                if(cashMovies != null){
                    deliverResult(cashMovies);
                }else {
                    ChangeIndicatorVisibility(true);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(List<Movie> data) {
                cashMovies = data;
                ChangeIndicatorVisibility(false);
                super.deliverResult(data);
            }

            @Override
            public List<Movie> loadInBackground() {
                URL mUrl= MovieAPI.Build_Sorted_Movies_URL(currentSortSelection);
                String response = MovieAPI.get_Response(mUrl);
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
        };




    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        ChangeIndicatorVisibility(false);
        if(data != null){
            mAdapter.UpdateListOfMovies(data);
            showMoviesList();
        }else {
            Toast.makeText(this, "e", Toast.LENGTH_SHORT).show();
            showErrorMessage(ERROR_LOADING_ERROR);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        GeneralData.mylog("Loader restarted");
    }

    private class MoviesLoadtask extends AsyncTask<URL,Void,List<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showMoviesList();
            ChangeIndicatorVisibility(true);
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
            ChangeIndicatorVisibility(false);
            if(movies != null){
                mAdapter.UpdateListOfMovies(movies);
                showMoviesList();
            }else {
                showErrorMessage(ERROR_LOADING_ERROR);
            }
        }
    }




}
