package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class RecipeView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String recipeId;
    private TextView recipeName;
    private TextView ingredientsview;
    private TextView methodView;

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
        ingredientsview = findViewById(R.id.recipeView_Ingredients);
        methodView = findViewById(R.id.recipeView_Method);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("recipes");
        recipeId = getIntent().getStringExtra("recipeId");

        // Set up WebView to display the YouTube video
        WebView webView = findViewById(R.id.webView);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/gH0MVoWvtl8?si=U4S-_cLhfs1TtDw-\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        reference.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addRecipeClass addRecipeClass = dataSnapshot.getValue(addRecipeClass.class);

                if(addRecipeClass != null){
                    String Name = addRecipeClass.getName();
                    String ingredients = addRecipeClass.getIngredients();
                    String method = addRecipeClass.getMethod();

                    recipeName.setText(Name);
                    ingredientsview.setText(ingredients);
                    methodView.setText(method);

                }else{
                    Toast.makeText(RecipeView.this,"No data", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RecipeView.this,"Database Error", Toast.LENGTH_LONG).show();
            }
        });

    }


    // Method to go back to Desserts activity
    public void GoBackToDesserts(View view) {
        startActivity(new Intent(this, Home.class));
    }
//
//    // Method to share the recipe on social media
//    public void shareRecipe(View view) {
//        String shareText = "Check out this amazing Raspberry Pie recipe! Ingredients:\n" +
//                "- Fresh Strawberries (4 Cups)\n" +
//                "- Sugar (2 Cups)\n" +
//                "- Lemon Juice (2 tbs)\n" +
//                "\nSteps:\n" +
//                "1. Wash the raspberries.\n" +
//                "2. Combine raspberries, sugar, and lemon juice.\n" +
//                "3. Boil for 10-15 minutes.";
//
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
//
//        // Optionally add a subject (used in email clients, etc.)
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Delicious Raspberry Pie Recipe");
//
//        // Show the sharing options
//        startActivity(Intent.createChooser(shareIntent, "Share Recipe via"));
//    }
}
