package com.abdelmun3m.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class Movie implements Parcelable {


    private static final String TAG = GeneralData.TAG+" : "+Movie.class.getSimpleName();
    public long Movie_DB_ID =-1;
    public String Movie_Id;
    public String OriginallTitle = "Movie Title";
    public String PosterImage ;
    public String Overview;
    public String RelaseDate;
    public long Vote_Average;
    public int favorite  = 0; //not favorite movie


    protected Movie(Parcel in) {
        Movie_Id = in.readString();
        Movie_DB_ID = in.readLong();
        OriginallTitle = in.readString();
        PosterImage = in.readString();
        Overview = in.readString();
        RelaseDate = in.readString();
        Vote_Average=in.readLong();
        favorite = in.readInt();
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
       String Image_URL = GeneralData.IMAGE_URL+this.PosterImage;
       Picasso.with(im.getContext()).load(Image_URL).error(android.R.drawable.ic_menu_gallery).into(im, new Callback() {
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
        dest.writeString(Movie_Id);
        dest.writeLong(Movie_DB_ID);
        dest.writeString(OriginallTitle);
        dest.writeString(PosterImage);
        dest.writeString(Overview);
        dest.writeString(RelaseDate);
        dest.writeLong(Vote_Average);
        dest.writeInt(favorite);
    }
}
