package com.abdelmun3m.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by abdelmun3m on 18/09/17.
 */

public class Movie implements Parcelable {


    private static final String TAG = General_Data.TAG+" : "+Movie.class.getSimpleName();

    public int Movie_Id;
    public String OriginallTitle = "Movie Title";
    public String PosterImage ;
    public String Overview;
    public String RelaseDate;
    public long Vote_Average;


    protected Movie(Parcel in) {
        Movie_Id = in.readInt();
        OriginallTitle = in.readString();
        PosterImage = in.readString();
        Overview = in.readString();
        RelaseDate = in.readString();
        Vote_Average=in.readLong();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie() {

    }


    public void loadMovieImage(ImageView im , final ProgressBar p){
       String Image_URL = General_Data.IMAGE_URL+this.PosterImage;
       Log.i(TAG,Image_URL);
       Picasso.with(im.getContext()).load(Image_URL).into(im, new Callback() {
           @Override
           public void onSuccess() {
                p.setVisibility(View.GONE);
           }

           @Override
           public void onError() {

           }
       });
   }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Movie_Id);
        dest.writeString(OriginallTitle);
        dest.writeString(PosterImage);
        dest.writeString(Overview);
        dest.writeString(RelaseDate);
        dest.writeLong(Vote_Average);
    }
}
