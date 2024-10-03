package com.example.test6;

import static java.io.File.createTempFile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.io.File;
import java.io.IOException;

public class UserHome extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = mAuth.getCurrentUser().getUid();
        TextView usernameText = findViewById(R.id.UserNameShow);


        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HelperClass userProfile = dataSnapshot.getValue(HelperClass.class);

                if(userProfile != null){
                    String user = userProfile.username;


                    usernameText.setText(user);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserHome.this,"", Toast.LENGTH_LONG).show();
            }
        });



        // Bottom navigation code
        // Start here
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.user);

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
                startActivity(new Intent(getApplicationContext(), Favourites.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.history) {
                startActivity(new Intent(getApplicationContext(), History.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.user) {
                return true;
            }
            return false;
        });
        // Ends here
    }

    public void AboutUs(View view) {
        startActivity(new Intent(this, AboutUs.class));
    }

    public void GoToUserProfileSettings(View view) {
        startActivity(new Intent(this, ProfileSettings.class));
    }
}