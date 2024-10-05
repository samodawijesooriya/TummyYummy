// IM/2021/076 (start)

package com.example.test6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewsList;

    public ReviewAdapter(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewsList.get(position);

        holder.reviewText.setText(review.getReviewText());
        holder.ratingBar.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewText;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewText = itemView.findViewById(R.id.reviewText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}

// IM/2021/076 (end)