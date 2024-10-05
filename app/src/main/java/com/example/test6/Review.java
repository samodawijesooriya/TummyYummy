// IM/2021/076 (start)

package com.example.test6;

public class Review {
    private String reviewId;
    private String reviewText;
    private float rating;  // Add rating
    private String userId;

    public Review() {
    }

    public Review(String reviewId, String reviewText, float rating, String userId) {
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.userId = userId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

// IM/2021/076 (end)