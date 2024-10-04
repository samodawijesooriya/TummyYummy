package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String userId;

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



        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = mAuth.getCurrentUser().getUid();
        TextView usernameText = findViewById(R.id.home_username);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HelperClass userProfile = dataSnapshot.getValue(HelperClass.class);

                if(userProfile != null){
                    String user = userProfile.username;

                    usernameText.setText("Hi! " + user);
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
                Intent intent = new Intent(Home.this, Signin.class);
                startActivity(intent);
                finish();
                Toast.makeText(Home.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        });

//        // Bottom navigation code
//        // Start here
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
            } else if (itemId == R.id.history) {
                startActivity(new Intent(getApplicationContext(), History.class));
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
            intent.putExtra("category", "Beverages");
        } else if (view.getId() == R.id.saladsBtn) {
            intent.putExtra("category", "Salads");
        }

        // Start CategoryActivity
        startActivity(intent);
    }


    public void GoToDesserts(View view) {
        startActivity(new Intent(this, Desserts.class));
    }
}