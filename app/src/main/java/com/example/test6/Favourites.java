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
//IM/2021/059 (start)
public class Favourites extends AppCompatActivity {

                                                                                                    // initialize the objects to the adapter
    private ArrayList<addRecipeClass> addRecipeClassArrayList;
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
                                                                                                    // Recycle view setup
        favRecyclerView = findViewById(R.id.favourites_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Favourites.this, 1);
        favRecyclerView.setLayoutManager(gridLayoutManager);
        loadFavoriteRecipes();                                                                      //  populate the RecyclerView with favorite recipes

        searchView = findViewById(R.id.favourites_searchView);                                      // Binds the search view from the layout
        searchView.clearFocus();
                                                                                                    // Sets up listeners for handling search input
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");    // set the database connection
        reference.child(FirebaseAuth.getInstance().getUid()).child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addRecipeClassArrayList.clear();                                                    // clear list before starting adding data
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String recipeId = ""+ds.child("recipeId").getValue();                      // we will only get recipeId here
                    addRecipeClass addRecipeClass = new addRecipeClass();                           // set id to addRecipeClass
                    addRecipeClass.setRecipeID(recipeId);

                    addRecipeClassArrayList.add(addRecipeClass);                                    // add recipes to list
                }
                adapterFav = new AdapterFav(Favourites.this, addRecipeClassArrayList);      // create an object for the addRecipeClass
                favRecyclerView.setAdapter(adapterFav);                                             // set adapter to recycle view
                adapterFav.notifyDataSetChanged();                                                  // set the data for the adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void searchList(String text){                                                           // Search for recipes within the favorite
        ArrayList<addRecipeClass> searchList = new ArrayList<>();
        for(addRecipeClass recipe: addRecipeClassArrayList){
            if(recipe.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(recipe);
            }
        }
        adapterFav.searchDatalist(searchList);
    }
//IM/2021/059 end
}
//IM/2021/103 End