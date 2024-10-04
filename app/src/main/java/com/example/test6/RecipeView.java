package com.example.test6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String recipeId;
    private TextView recipeName;
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

        // Initialize UI components
        recipeName = findViewById(R.id.recipeView_RecipeName);
        ingredientsView = findViewById(R.id.recipeView_Ingredients);
        methodView = findViewById(R.id.recipeView_Method);
        videoView = findViewById(R.id.RecipeView_videoView);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("recipes");
        recipeId = getIntent().getStringExtra("recipeId");

        // Set up WebView to display the YouTube video
//        WebView webView = findViewById(R.id.webView);
//        String video1 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/gH0MVoWvtl8?si=U4S-_cLhfs1TtDw-\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
//        webView.loadData(video1, "text/html", "utf-8");
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new WebChromeClient());

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

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
        VideoView videoView = findViewById(R.id.RecipeView_videoView);

        // Set video URI to VideoView
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);

        // Add media controls to play, pause, etc.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Start video playback
        videoView.start();
    }

}
