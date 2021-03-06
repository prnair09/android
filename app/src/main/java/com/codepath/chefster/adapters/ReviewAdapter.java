package com.codepath.chefster.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Review;
import com.codepath.chefster.models.User;
import com.codepath.chefster.viewholders.ReviewViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        holder.getTvReviewName().setText("Anonymous");
        holder.getTvReview().setText(review.getDescription());
        if (review.getRating() != null)
            holder.getRbReview().setRating(review.getRating().floatValue());
        if (user != null) {
            String userPhoto = user.getImageUrl();
            holder.getTvReviewName().setText(user.getFirstName());
            if (userPhoto != "") {
                Glide.with(getContext()).load(user.getImageUrl()).asBitmap().centerCrop()
                        .into(holder.getIvReviewProfile());
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = review.getDate();
        if (date != null) {
            String reviewDate = getRelativeTimeAgo(date.toString());//dateFormat.format(review.getDate());
            holder.getTvReviewDate().setText(reviewDate);
        } else {
            holder.getTvReviewDate().setText("12/03/2016");
        }
    }

    @Override
    public int getItemCount() {
        if (reviewList != null && !reviewList.isEmpty()) {
            return reviewList.size();
        }
        return 0;
    }

    /*
    * getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    */
    public String getRelativeTimeAgo(String rawJsonDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
