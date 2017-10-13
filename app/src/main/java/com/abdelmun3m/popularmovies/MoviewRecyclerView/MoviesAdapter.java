package com.abdelmun3m.popularmovies.MoviewRecyclerView;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abdelmun3m.popularmovies.Movie;
import com.abdelmun3m.popularmovies.MoviesProvider.MoviesContract;
import com.abdelmun3m.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>{


    private List<Movie> MovieList = new ArrayList<>();
    private MovieClick movieClick =null;
    Cursor mCursor;
    public MoviesAdapter(MovieClick c){
        this.movieClick = c;
    }


    public void convertCursorToMovies(Cursor mCursorData){
        List<Movie> myList = new ArrayList<>();
        while (mCursorData.moveToNext()){
            Movie movie = new Movie();
            movie.Movie_Id = mCursorData.getString(MoviesContract.FavoriteMoviesEntity.INDEX_COLUMN_MOVIE_ID);
            movie.OriginallTitle = mCursorData.getString(MoviesContract.FavoriteMoviesEntity.INDEX_COLUMN_ORIGINAL_TITLE);
            movie.Overview = mCursorData.getString(MoviesContract.FavoriteMoviesEntity.INDEX_COLUMN_OVERVIEW);
            movie.PosterImage=mCursorData.getString(MoviesContract.FavoriteMoviesEntity.INDEX_COLUMN_POSTER_IMAGE);
            movie.RelaseDate = mCursorData.getString(MoviesContract.FavoriteMoviesEntity.INDEX_COLUMN_RELEASE_DATE);
            movie.Vote_Average = mCursorData.getLong(MoviesContract.FavoriteMoviesEntity.INDEX_COLUMN_VOTE_RATE);
            if(movie != null){
                myList.add(movie);
            }
        }
        UpdateListOfMovies(myList);
    }

    public void UpdateListOfMovies(List<Movie> newList){
        this.MovieList = newList;
        notifyDataSetChanged();
    }
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MoviesViewHolder mViewHolder;
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false);
        mViewHolder = new MoviesViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

        holder.bind(MovieList.get(position));
    }

    @Override
    public int getItemCount() {
        return MovieList.size();
    }



    /**
     *
     *  class MoviewViewHolder
     * **/

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView originalTitke ;
        ImageView movieImage;
        Movie currentMovie = null;
        ProgressBar ImageLoad ;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            originalTitke = (TextView) itemView.findViewById(R.id.movie_original_title);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_img);
            ImageLoad = (ProgressBar) itemView.findViewById(R.id.image_load);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie m){
            this.currentMovie = m;
            originalTitke.setText(m.OriginallTitle);
            m.loadMovieImage(this.movieImage,this.ImageLoad);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == itemView.getId()) movieClick.onMovieClick(this.currentMovie);
        }
    }

    /**
     *
     * Click Event Listener interface
     * **/

    public interface MovieClick{
        void onMovieClick(Movie m);
    }
}
