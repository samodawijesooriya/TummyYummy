package com.example.test6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    // IM/2021/059 (Start)
    // define variable and objects
    GridView gridView;
    ArrayList<addRecipeClass> recipeList;
    Adapter1 adapter1;
    protected FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String userId;
    private ShapeableImageView profileImageView;

    // oncreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // creating objects
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = mAuth.getCurrentUser().getUid();

        // geting the items from the xml file
        TextView usernameText = findViewById(R.id.home_username);
        profileImageView = findViewById(R.id.imageView4);

        // refer to the database and retrive the data
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HelperClass userProfile = dataSnapshot.getValue(HelperClass.class);

                if(userProfile != null){
                    // printing data
                    String user = userProfile.username;
                    usernameText.setText("Hi " + user+"!");

                    // check if the imageUrl is null
                    if (userProfile.imgUrl != null) {
                        // set the image
                        Glide.with(Home.this).load(userProfile.getImgUrl()).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this,"", Toast.LENGTH_LONG).show();
            }
        });

        ImageView logoutImg = findViewById(R.id.home_logout);
        logoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

                SharedPreferences sharedPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clear all preferences including isLoggedIn flag
                editor.apply();

                Intent intent = new Intent(Home.this, Signin.class);
                startActivity(intent);
                finish();
                Toast.makeText(Home.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize GridView
        gridView = findViewById(R.id.home_gridView);

        // Initialize recipe list
        recipeList = new ArrayList<>();

        // Set up the adapter
        adapter1 = new Adapter1(recipeList, this);
        gridView.setAdapter(adapter1);

        // Fetch only recipes with duration <= 20 minutes and limit the result to 2
        DatabaseReference recipeReference = FirebaseDatabase.getInstance().getReference("recipes");
        recipeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (count >= 2) break;

                    addRecipeClass recipe = snapshot.getValue(addRecipeClass.class);

                    // Check if duration is <= 20
                    if (recipe != null && recipe.getDuration() != null) {
                        try {
                            int durationInMinutes = Integer.parseInt(recipe.getDuration().replaceAll("[^0-9]", ""));
                            if (durationInMinutes <= 20) {
                                recipeList.add(recipe);
                                count++;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                adapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
            }
        });

        // Bottom navigation code
        // Start here
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.btnhome);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();

            if (itemId == R.id.btnhome) {
                return true;
            } else if (itemId == R.id.btnedit) {
                startActivity(new Intent(getApplicationContext(), MyRecipe.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.fav) {
                startActivity(new Intent(getApplicationContext(), Favourites.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.user) {
                startActivity(new Intent(getApplicationContext(), UserHome.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
        // Ends here
    }

    // call the catogory.java file and send the choosen category for category.java
    public void onCategoryClick(View view) {
        Intent intent = new Intent(this, Category.class);

        // Use if-else instead of switch
        if (view.getId() == R.id.home_allbtn) {
            intent.putExtra("category", "All");
        } else if (view.getId() == R.id.dessertBtn) {
            intent.putExtra("category", "Desserts");
        } else if (view.getId() == R.id.snacksBtn) {
            intent.putExtra("category", "Snacks");
        } else if (view.getId() == R.id.beveragesBtn) {
            intent.putExtra("category", "Beverage");
        } else if (view.getId() == R.id.saladsBtn) {
            intent.putExtra("category", "Salads");
        } else if (view.getId() == R.id.soupsBtn) {
            intent.putExtra("category", "Soups");
        }

        // Start CategoryActivity
        startActivity(intent);
    }



    public void GoToDesserts(View view) {
        startActivity(new Intent(this, Desserts.class));
    }
    // IM/2021/059 (end)

    // IM/2021/103 (start)
    // make the user to stay login in the app
    @Override
    public void onBackPressed() {
        // Show a confirmation dialog before exiting
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Close the app
                    finishAffinity();  // Exits the app
                })
                .setNegativeButton("No", null)
                .show();
    }

    // IM/2021/103 (end)
}