package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// IM/2021/082 (Start)

public class Category extends AppCompatActivity {

    ArrayList<addRecipeClass> recipeList;
    MyAdapter2 myAdapter2;
    RecyclerView recyclerView;
    SearchView searchView;
    private TextView categoryTitle;
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

        recyclerView = findViewById(R.id.category_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Category.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        searchView = findViewById(R.id.category_searchView);
        searchView.clearFocus();

        recipeList = new ArrayList<>();
        myAdapter2 = new MyAdapter2(Category.this, recipeList);
        recyclerView.setAdapter(myAdapter2);

        // Get the category name from the Intent | from Home Activity
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        if (category == null) {
            category = "All";
        }

        categoryTitle = findViewById(R.id.category_title);
        categoryTitle.setText(category);

        loadRecipesByCategory(category);

        // Set up TextWatcher for search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchList(s);
                return true;
            }
        });

    }

    // Load recipes based on category
    private void loadRecipesByCategory(String category) {
        // Reference to the Firebase node
        reference = FirebaseDatabase.getInstance().getReference("recipes");

        // filter recipes by category
        Query query;
        if (category.equals("All")) {
            query = reference;
        } else {
            query = reference.orderByChild("category").equalTo(category);
        }

        // Get the data from Firebase
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeList.clear();

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    addRecipeClass recipe = recipeSnapshot.getValue(addRecipeClass.class);
                    recipeList.add(recipe);
                }

                myAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public  void searchList(String text){
        ArrayList<addRecipeClass> searchList = new ArrayList<>();
        for(addRecipeClass recipe: recipeList){
            if(recipe.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(recipe);
            }
        }
        myAdapter2.searchDatalist(searchList);
    }

    public void GoHomeFromCategory(View view) {
        startActivity(new Intent(this, Home.class));
    }

}

//IM/2021/082 (End)
