package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
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

    GridView gridView;
    ArrayList<addRecipeClass> recipeList;
    Adapter1 adapter1;

    private TextView categoryTitle; // Make sure to add this in your layout XML
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

        // Initialize the GridView
        gridView = findViewById(R.id.gridView);

        // Initialize your recipe list
        recipeList = new ArrayList<>();

        // Set up the adapter and assign it to the GridView
        adapter1 = new Adapter1(recipeList, this);
        gridView.setAdapter(adapter1);

        // Get the category name from the Intent
        Intent intent = getIntent();
        String category = intent.getStringExtra("category"); // Get the category passed from Home activity

        // Optionally, update the UI to show the category title
        categoryTitle = findViewById(R.id.category_title); // Ensure you have a TextView with this ID in your layout
        categoryTitle.setText(category); // Set the category title

        // Load recipes based on the received category
        loadRecipesByCategory(category);
    }

    // Method to load recipes based on category
    private void loadRecipesByCategory(String category) {
        // Reference to the Firebase 'recipes' node
        reference = FirebaseDatabase.getInstance().getReference("recipes");

        // Firebase query to filter recipes by category
        Query query;
        if (category.equals("All")) {
            query = reference;  // No filtering, fetch all recipes
        } else {
            query = reference.orderByChild("category").equalTo(category);  // Filter by category
        }

        // Use ValueEventListener to get the data from Firebase
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeList.clear(); // Clear the list before adding new data

                // Loop through each recipe in the snapshot
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    // Assuming addRecipeClass is your model class for recipes
                    addRecipeClass recipe = recipeSnapshot.getValue(addRecipeClass.class);
                    recipeList.add(recipe);  // Add each recipe to the list
                }

                // Notify the adapter that data has changed
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any database errors
            }
        });
    }

    // Navigation method to go back to home screen
    public void GoHomeFromCategory(View view) {
        startActivity(new Intent(this, Home.class));
    }

}
