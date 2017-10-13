package com.abdelmun3m.popularmovies.MoviewRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdelmun3m.popularmovies.R;
import com.abdelmun3m.popularmovies.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdelmun3m on 12/10/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {


    List<Review> reviews= new ArrayList<>();
    reviewClick mReviewClick;
    public ReviewAdapter(reviewClick r){
        mReviewClick = r;
    }

    public void updateReviews(List<Review> r){
        this.reviews = r;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReviewViewHolder holder ;
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
        holder = new ReviewViewHolder(viewHolder);
        return holder;
    }
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        if(reviews !=null)  return reviews.size();
        return 0;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTv_review;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            mTv_review =(TextView) itemView.findViewById(R.id.tv_review);
            itemView.setOnClickListener(this);
        }

        public void bind(Review r){
            mTv_review.setText(r.author);
            itemView.setTag(r.url);
        }
        @Override
        public void onClick(View v) {
            if(v.getId() == itemView.getId()) {mReviewClick.onReviewClick((String) itemView.getTag());}
        }
    }

    public interface reviewClick{
        void onReviewClick(String url);
    }
}
