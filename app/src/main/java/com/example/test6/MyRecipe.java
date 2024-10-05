package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRecipe extends AppCompatActivity {

    GridView gridView;
    ArrayList<addRecipeClass> addRecipeList;
    Adapter1 adapter1;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recipes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridView = findViewById(R.id.gridView);

        addRecipeList = new ArrayList<>();
        adapter1 = new Adapter1(addRecipeList, this);
        gridView.setAdapter(adapter1);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    addRecipeClass addRecipeClass = dataSnapshot1.getValue(com.example.test6.addRecipeClass.class);
                    addRecipeList.add(addRecipeClass);
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // Bottom navigation code
        // Start here
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.btnedit);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();

            if (itemId == R.id.btnhome) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.btnedit) {
                return true;
            } else if (itemId == R.id.fav) {
                startActivity(new Intent(getApplicationContext(), Favourites.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
//            } else if (itemId == R.id.history) {
//                startActivity(new Intent(getApplicationContext(), History.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//                return true;
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

    public void GoToAddRecipe(View view) {
        startActivity(new Intent(this, AddRecipe.class));
    }
}