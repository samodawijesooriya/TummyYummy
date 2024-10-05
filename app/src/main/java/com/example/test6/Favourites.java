package com.example.test6;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Favourites extends AppCompatActivity {

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
        //        // Bottom navigation code
//        // Start here
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
}