package com.example.test6;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Objects;

public class ViewEditDeleteRecipe extends AppCompatActivity {

    private String recipeId;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private TextView recipeName;
    private ImageView backBtn;
    private TextView ingredientsView;
    private TextView methodView;
    private addRecipeClass currentRecipe;// Store the current recipe data
    private VideoView videoView;
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_edit_delete_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button edit = findViewById(R.id.editViewRecipe_EditBtn);
        recipeName = findViewById(R.id.ViewEditDelete_RecipeName);
        ingredientsView = findViewById(R.id.recipeView_Ingredients);
        methodView = findViewById(R.id.recipeView_Method);
        videoView = findViewById(R.id.ViewEditDelete_videoView);
        backBtn = findViewById(R.id.ViewEditDelete_backBtn);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("recipes");
        recipeId = getIntent().getStringExtra("recipeId");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEditDeleteRecipe.this, EditRecipe.class);
                intent.putExtra("recipeId", recipeId);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEditDeleteRecipe.this, Category.class);
                startActivity(intent);
            }
        });

        Button deleteRecipeBtn = findViewById(R.id.editViewRecipe_DeleteBtn);

        deleteRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewEditDeleteRecipe.this);
                dialog.setTitle("Delete Recipe?");
                dialog.setMessage("Deleting this Recipe will completely removing your "+
                        "this recipe from the system and you won't be able to access them.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog progressDialog = new AlertDialog.Builder(ViewEditDeleteRecipe.this)
                                .setView(R.layout.dialog_progress) // Use the custom layout
                                .setCancelable(false) // Prevent cancellation
                                .create();

                        progressDialog.show();

                        reference.child(recipeId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(ViewEditDeleteRecipe.this, "Recipe Deleted", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ViewEditDeleteRecipe.this, Category.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ViewEditDeleteRecipe.this,
                                            Objects.requireNonNull(task.getException()).getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });


        // Fetch recipe details from the database
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
                    setVideo(videoUrlnew);
                } else {
                    Toast.makeText(ViewEditDeleteRecipe.this, "No data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewEditDeleteRecipe.this, "Database Error", Toast.LENGTH_LONG).show();
            }
        });


        ImageView reviewImage = findViewById(R.id.ReviewImage);

        reviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewPopup();
            }
        });

    }

    // Method to share the recipe when the share button is clicked
    public void shareRecipe(View view) {
        if (currentRecipe != null) {
            // Format the recipe details for sharing
            String shareText = "Check out this amazing recipe: " + currentRecipe.getName() + "!\n\n" +
                    "Ingredients:\n" + currentRecipe.getIngredients() + "\n\n" +
                    "Method:\n" + currentRecipe.getMethod();

            // Create the share intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            // Optionally add a subject (used in email clients, etc.)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Delicious " + currentRecipe.getName() + " Recipe");

            // Show the sharing options
            startActivity(Intent.createChooser(shareIntent, "Share Recipe via"));
        } else {
            Toast.makeText(this, "Recipe not loaded yet!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVideo(String videoUrl) {
        VideoView videoView = findViewById(R.id.ViewEditDelete_videoView);
        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);

            // Add media controls to play, pause, etc.
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            // Start video playback
            videoView.start();
        } else {
            Toast.makeText(this, "No video available", Toast.LENGTH_LONG).show();
        }
    }

    private void showReviewPopup() {
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
                    Toast.makeText(ViewEditDeleteRecipe.this, "Please enter a review and select a rating", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show the popup
        popupWindow.showAtLocation(findViewById(R.id.main), android.view.Gravity.CENTER, 0, 0);
    }

    private void loadPastReviews(List<Review> reviewsList, ReviewAdapter adapter) {
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
                Toast.makeText(ViewEditDeleteRecipe.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitReview(String reviewText, float rating) {
        DatabaseReference reviewsRef = reference.child(recipeId).child("reviews");
        String reviewId = reviewsRef.push().getKey();

        Review newReview = new Review(reviewId, reviewText, rating, mAuth.getCurrentUser().getUid()); // Pass rating to Review constructor

        reviewsRef.child(reviewId).setValue(newReview).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ViewEditDeleteRecipe.this, "Review submitted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ViewEditDeleteRecipe.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
            }
        });
    }
}