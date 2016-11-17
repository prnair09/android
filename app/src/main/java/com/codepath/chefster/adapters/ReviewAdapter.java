package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Review;
import com.codepath.chefster.models.User;
import com.codepath.chefster.viewholders.ReviewViewHolder;

import java.util.List;

/**
 * Created by Hezi Eliyahu on 16/11/2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public Context getContext() {
        return context;
    }

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        User user = review.getUser();

        holder.getTvReviewName().setText("Hezi");
        holder.getTvReview().setText(review.getDescription());
        if (review.getRating() != null)
            holder.getRbReview().setRating(review.getRating().floatValue());
    }

    @Override
    public int getItemCount() {
        if (reviewList != null && ! reviewList.isEmpty()) {
            return reviewList.size();
        }
        return 0;
    }
}
