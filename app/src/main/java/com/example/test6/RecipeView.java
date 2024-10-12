// IM/2021/104 (start)

package com.example.test6;

import static com.example.test6.ViewEditDeleteRecipe.addToFavorite;
import static com.example.test6.ViewEditDeleteRecipe.removeFromFavorites;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RecipeView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    boolean isInMyFavorite = false;
    private DatabaseReference reference;
    private String recipeId;
    private TextView recipeName;
    private ImageView bookMark, backBtn;
    private TextView ingredientsView;
    private TextView methodView;
    private addRecipeClass currentRecipe;// Store the current recipe data
    private VideoView videoView;
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recipeName = findViewById(R.id.recipeView_RecipeName);
        ingredientsView = findViewById(R.id.recipeView_Ingredients);
        methodView = findViewById(R.id.recipeView_Method);
        videoView = findViewById(R.id.RecipeView_videoView);
        bookMark = findViewById(R.id.recipeViewBookmarkBorder);
        backBtn = findViewById(R.id.BackBtn);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("recipes");
        recipeId = getIntent().getStringExtra("recipeId");

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        if (mAuth.getCurrentUser() != null){
            checkIsFavorite();
        }

        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() == null){
                    Toast.makeText(RecipeView.this, "You are not logged in", Toast.LENGTH_SHORT).show();
                }else{
                    if(isInMyFavorite){
                        removeFromFavorites(RecipeView.this, recipeId);
                    }else{
                        addToFavorite(RecipeView.this, recipeId);
                    }
                }
            }
        });

        reference.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentRecipe = dataSnapshot.getValue(addRecipeClass.class);

                if (currentRecipe != null) {
                    String Name = currentRecipe.getName();
                    String ingredients = currentRecipe.getIngredients();
                    String method = currentRecipe.getMethod();


                    recipeName.setText(Name);
                    ingredientsView.setText(ingredients);
                    methodView.setText(method);

                    String videoUrlnew = currentRecipe.getVideoUrl();
                    if( videoUrlnew != null){
                        setVideo(videoUrlnew);
                    }
                } else {
                    Toast.makeText(RecipeView.this, "No data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RecipeView.this, "Database Error", Toast.LENGTH_LONG).show();
            }
        });

        // back btn setOnClickListener
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeView.this, Category.class);
                startActivity(intent);
            }
        });

        // set the the review image
        ImageView reviewImage = findViewById(R.id.RecipeViewReviewImage);

        reviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewPopup();
            }
        });
    }

    public void shareRecipe(View view) {
        if (currentRecipe != null) {
            String shareText = "Check out this amazing recipe: " + currentRecipe.getName() + "!\n\n" +
                    "Ingredients:\n" + currentRecipe.getIngredients() + "\n\n" +
                    "Method:\n" + currentRecipe.getMethod();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Delicious " + currentRecipe.getName() + " Recipe");

            startActivity(Intent.createChooser(shareIntent, "Share Recipe via"));
        } else {
            Toast.makeText(this, "Recipe not loaded yet!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVideo(String videoUrl) {
        VideoView videoView = findViewById(R.id.RecipeView_videoView);

        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();
    }

    public void loadPastReviews(List<Review> reviewsList, ReviewAdapter adapter) {
        DatabaseReference reviewsRef = reference.child(recipeId).child("reviews");

        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewsList.clear();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    reviewsList.add(review);
                }
                adapter.notifyDataSetChanged(); // Notify adapter to refresh the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecipeView.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // submit the review func
    public void submitReview(String reviewText, float rating) {
        DatabaseReference reviewsRef = reference.child(recipeId).child("reviews");
        String reviewId = reviewsRef.push().getKey();

        Review newReview = new Review(reviewId, reviewText, rating, mAuth.getCurrentUser().getUid()); // Pass rating to Review constructor

        reviewsRef.child(reviewId).setValue(newReview).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RecipeView.this, "Review submitted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RecipeView.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // show popup view for the ratings
    public void showReviewPopup() {
        // Create the popup window for adding and viewing reviews
        View popupView = getLayoutInflater().inflate(R.layout.popup_review, null);
        final android.widget.PopupWindow popupWindow = new android.widget.PopupWindow(popupView,
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);

        // Find views inside the popup
        EditText reviewInput = popupView.findViewById(R.id.reviewInput);
        RatingBar ratingBar = popupView.findViewById(R.id.ratingBar); // New RatingBar
        Button submitReviewBtn = popupView.findViewById(R.id.submitReviewBtn);
        RecyclerView reviewsRecyclerView = popupView.findViewById(R.id.reviewsRecyclerView);

        // Setup RecyclerView
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Review> reviewsList = new ArrayList<>();
        ReviewAdapter adapter = new ReviewAdapter(reviewsList);
        reviewsRecyclerView.setAdapter(adapter);

        // Load past reviews and update the adapter
        loadPastReviews(reviewsList, adapter);


        // Submit review button click listener
        submitReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = reviewInput.getText().toString();
                float rating = ratingBar.getRating(); // Get the selected rating

                if (!reviewText.isEmpty() && rating > 0) {
                    submitReview(reviewText, rating);
                    reviewInput.setText(""); // Clear input field
                    ratingBar.setRating(0); // Reset rating
                    popupWindow.dismiss(); // Close popup after submission
                } else {
                    Toast.makeText(RecipeView.this, "Please enter a review and select a rating", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show the popup
        popupWindow.showAtLocation(findViewById(R.id.main), android.view.Gravity.CENTER, 0, 0);
    }

    private void checkIsFavorite(){
        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("users");
        referenceUser.child(mAuth.getUid()).child("favorite").child(recipeId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        isInMyFavorite = dataSnapshot.exists();
                        if(isInMyFavorite){
                            bookMark.setImageResource(R.drawable.baseline_bookmark_24);
                        }else{
                            bookMark.setImageResource(R.drawable.baseline_bookmark_border_24);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

// IM/2021/104 (end)