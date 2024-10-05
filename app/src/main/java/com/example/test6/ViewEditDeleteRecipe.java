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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ViewEditDeleteRecipe extends AppCompatActivity {

    private String recipeId;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private TextView recipeName;
    private ImageView backBtn;
    private TextView ingredientsView;
    private TextView methodView;
    private addRecipeClass currentRecipe1;// Store the current recipe data
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
                currentRecipe1 = dataSnapshot.getValue(addRecipeClass.class);

                if (currentRecipe1 != null) {
                    String Name = currentRecipe1.getName();
                    String ingredients = currentRecipe1.getIngredients();
                    String method = currentRecipe1.getMethod();


                    recipeName.setText(Name);
                    ingredientsView.setText(ingredients);
                    methodView.setText(method);

                    String videoUrlnew = currentRecipe1.getVideoUrl();
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

    }

    // Method to share the recipe when the share button is clicked
    public void shareRecipe(View view) {
        if (currentRecipe1 != null) {
            // Format the recipe details for sharing
            String shareText = "Check out this amazing recipe: " + currentRecipe1.getName() + "!\n\n" +
                    "Ingredients:\n" + currentRecipe1.getIngredients() + "\n\n" +
                    "Method:\n" + currentRecipe1.getMethod();

            // Create the share intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            // Optionally add a subject (used in email clients, etc.)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Delicious " + currentRecipe1.getName() + " Recipe");

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
}