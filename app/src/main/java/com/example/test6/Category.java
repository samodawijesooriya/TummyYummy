package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Category extends AppCompatActivity {
    private TextView categoryTitle;
    private LinearLayout recipeContainer;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryTitle = findViewById(R.id.category_title);
        recipeContainer = findViewById(R.id.recipe_container);

        String category = getIntent().getStringExtra("category");
        categoryTitle.setText(category);

        loadRecipesByCategory(category);
    }

    private void loadRecipesByCategory(String category) {
        // Reference to your Firebase 'recipes' node
        reference = FirebaseDatabase.getInstance().getReference("recipes");

        // Firebase query to filter recipes by category
        Query query;
        if (category.equals("All")) {
            query = reference;;  // No filtering, fetch all recipes
        } else {
            query = reference.orderByChild("category").equalTo(category);  // Filter by category
        }

        // Use ValueEventListener to get the data
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Recipe> filteredRecipes = new ArrayList<>();
//
//                // Loop through each recipe in the snapshot
//                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
//                    // Assuming you have a Recipe model class
//                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
//                    filteredRecipes.add(recipe);
//                }
//
//                // Now you have a list of filtered recipes, you can display them
//                displayRecipes(filteredRecipes);
//            }
//
//            private void displayRecipes(List<Recipe> recipes) {
//                // Assuming you have a RecyclerView to display the recipes
//                RecyclerView recyclerView = findViewById(R.id.recipe_recyclerView);
//                recyclerView.setLayoutManager(new LinearLayoutManager(Category.this));
//
//                // Assuming you have a RecipeAdapter that takes a list of recipes
//                RecipeAdapter adapter = new RecipeAdapter(recipes);
//                recyclerView.setAdapter(adapter);
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle any database errors
//            }
//        });
    }

    public void GoHomeFromCategory(View view) {
        startActivity(new Intent(this, Home.class));
    }
}