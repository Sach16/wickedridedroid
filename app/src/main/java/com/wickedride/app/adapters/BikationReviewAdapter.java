package com.wickedride.app.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wickedride.app.R;
import com.wickedride.app.models.Review;
import com.wickedride.app.views.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class BikationReviewAdapter extends RecyclerView.Adapter<BikationReviewAdapter.BikationReviewViewHolder> {
    private final Activity mActivity;
    private ArrayList<Review> mBikationReviews;

    public BikationReviewAdapter(Activity activity, ArrayList<Review> bikationReviews) {
        this.mBikationReviews = bikationReviews;
        this.mActivity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BikationReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_review, parent, false);
        BikationReviewViewHolder viewHolder = new BikationReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BikationReviewViewHolder holder, int position) {
        Picasso.with(mActivity).load(mBikationReviews.get(position).getReviewer().getImage().getFull()).placeholder(R.drawable.place_holder).fit().centerCrop().into(holder.bikerImage);
        holder.bikerComment.setText(mBikationReviews.get(position).getTitle());
        holder.bikerRating.setRating(Float.parseFloat((mBikationReviews.get(position).getRating())));
        holder.byBiker.setText("By: "+mBikationReviews.get(position).getReviewer().getName() +" "+ mBikationReviews.get(position).getCreatedAt());
        holder.bikerExperience.setText(mBikationReviews.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return mBikationReviews.size();
    }

    class BikationReviewViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.imgAvatar)
        CircleImageView bikerImage;
        @InjectView(R.id.biker_comment)
        TextView bikerComment;
        @InjectView(R.id.biker_rating)
        RatingBar bikerRating;
        @InjectView(R.id.by_biker)
        TextView byBiker;
        @InjectView(R.id.biker_experience)
        TextView bikerExperience;

        public BikationReviewViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
