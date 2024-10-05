package com.example.test6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test6.R;
import com.example.test6.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewsList; // Define the reviewsList

    // Constructor to pass the reviews list
    public ReviewAdapter(List<Review> reviewsList) {
        this.reviewsList = reviewsList; // Initialize the reviewsList in the constructor
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your review item layout here (replace R.layout.item_review with your actual layout resource)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // Get the current review object from the list
        Review review = reviewsList.get(position);

        // Set the review text and rating in the ViewHolder
        holder.reviewText.setText(review.getReviewText());
        holder.ratingBar.setRating(review.getRating()); // Set the rating in RatingBar
    }

    @Override
    public int getItemCount() {
        return reviewsList.size(); // Return the size of the reviewsList
    }

    // ViewHolder class to hold the review item views
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewText;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views (replace these IDs with your actual view IDs)
            reviewText = itemView.findViewById(R.id.reviewText);
            ratingBar = itemView.findViewById(R.id.ratingBar); // This is the RatingBar for displaying the rating
        }
    }
}
