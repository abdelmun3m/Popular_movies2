package com.abdelmun3m.popularmovies.MoviewRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdelmun3m.popularmovies.GeneralData;
import com.abdelmun3m.popularmovies.R;


/**
 * Created by abdelmun3m on 08/10/17.
 */

public class TrailerRecyclerView extends RecyclerView.Adapter<TrailerRecyclerView.TrailerViewHolder> {



    private String[] listOfUrl ;
    private trailerClicke myTrailerClicke;

    public TrailerRecyclerView(trailerClicke trailerClickeObject){
        myTrailerClicke= trailerClickeObject;
    }

    public void updateTrailers(String[] newList) {
        listOfUrl = newList;
        notifyDataSetChanged();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TrailerViewHolder holder ;
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item,parent,false);
        holder = new TrailerViewHolder(viewHolder);
        return holder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(position,listOfUrl[position]);
    }

    @Override
    public int getItemCount() {
        if (listOfUrl != null) return listOfUrl.length;
        return 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView trailerId ;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerId = (TextView) itemView.findViewById(R.id.tv_trailer_ID);
            itemView.setOnClickListener(this);
        }

        public void bind(int id , String url){
            itemView.setTag(GeneralData.YOUTUBE_MOVIE+url);
            trailerId.setText(itemView.getContext().getResources().getString(R.string.trailerID,String.valueOf((id+1))));
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == itemView.getId()) myTrailerClicke.onTrailerClieck((String) itemView.getTag());
        }
    }

    public interface trailerClicke{

        void onTrailerClieck(String url);
    }
}
