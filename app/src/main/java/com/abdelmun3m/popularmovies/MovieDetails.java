package com.abdelmun3m.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MovieDetails extends AppCompatActivity {

    private Movie CurrentMovie = null;
    private ImageView poster ;
    private TextView originaltitle,overView, voteAverage,releadeDate ;
    private ProgressBar pb_load;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent in = getIntent();
        if(in.hasExtra(GeneralData.INTENT_TAG)){
            CurrentMovie = in.getParcelableExtra(GeneralData.INTENT_TAG);
        }

        if(CurrentMovie != null){
            originaltitle = (TextView) findViewById(R.id.tv_md_Originaltitle);
            overView = (TextView) findViewById(R.id.tv_md_overview);
            voteAverage=(TextView) findViewById(R.id.tv_md_vote_average);
            releadeDate =(TextView) findViewById(R.id.tv_md_ReleaseDate);
            poster = (ImageView) findViewById(R.id.im_md_Image);
            pb_load = (ProgressBar) findViewById(R.id.pb_md_image_load);

            originaltitle.setText(CurrentMovie.OriginallTitle);
            CurrentMovie.loadMovieImage(poster,pb_load);
            releadeDate.setText(CurrentMovie.RelaseDate);
            voteAverage.setText(String.valueOf(CurrentMovie.Vote_Average));
            overView.setText(CurrentMovie.Overview);
        }


    }
}
