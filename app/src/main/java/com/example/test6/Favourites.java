//IM/2021/103 Start
package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favourites extends AppCompatActivity {

    // array to hold the recipes
    private ArrayList<addRecipeClass> addRecipeClassArrayList;
    // adapter to set in recycleView
    private AdapterFav adapterFav;
    SearchView searchView;
    private RecyclerView favRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourites);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        favRecyclerView = findViewById(R.id.favourites_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Favourites.this, 1);
        favRecyclerView.setLayoutManager(gridLayoutManager);
        loadFavoriteRecipes();

        searchView = findViewById(R.id.favourites_searchView);
        searchView.clearFocus();

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


        // Bottom navigation code
        BottomNavigationView bottomNavigationView = findViewById(R.id.favourites_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.fav);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();

            if (itemId == R.id.btnhome) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.btnedit) {
                startActivity(new Intent(getApplicationContext(), MyRecipe.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.fav) {
                return true;

            } else if (itemId == R.id.user) {
                startActivity(new Intent(getApplicationContext(), UserHome.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadFavoriteRecipes() {
        // init list
        addRecipeClassArrayList = new ArrayList<>();

        // load favorite books from database
        // user > userId > favorite
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getUid()).child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear list before starting adding data
                addRecipeClassArrayList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    // we will only get recipeId here
                    String recipeId = ""+ds.child("recipeId").getValue();

                    // set id to addRecipeClass
                    addRecipeClass addRecipeClass = new addRecipeClass();
                    addRecipeClass.setRecipeID(recipeId);

                    // add recipes to list
                    addRecipeClassArrayList.add(addRecipeClass);
                }

                adapterFav = new AdapterFav(Favourites.this, addRecipeClassArrayList);
                // set adapter to recycle view
                favRecyclerView.setAdapter(adapterFav);

                adapterFav.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void searchList(String text){
        ArrayList<addRecipeClass> searchList = new ArrayList<>();
        for(addRecipeClass recipe: addRecipeClassArrayList){
            if(recipe.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(recipe);
            }
        }
        adapterFav.searchDatalist(searchList);
    }

}
//IM/2021/103 End