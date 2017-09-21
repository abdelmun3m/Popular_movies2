package com.abdelmun3m.popularmovies.MoviewRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abdelmun3m.popularmovies.Movie;
import com.abdelmun3m.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>{


    private List<Movie> MovieList = new ArrayList<>();
    private MovieClick movieClick =null;

    public MoviesAdapter(MovieClick c){
        this.movieClick = c;
    }

    public void UpdateListOfMovies(List<Movie> newList ){
        this.MovieList = newList;
        notifyDataSetChanged();
    }
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MoviesViewHolder mViewHolder;
        View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false);
        mViewHolder = new MoviesViewHolder(mview);
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
