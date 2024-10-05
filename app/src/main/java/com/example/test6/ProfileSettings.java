package com.example.test6;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileSettings extends AppCompatActivity {

    private DatabaseReference reference;
    private String userId;
    protected FirebaseAuth mAuth;
    private TextView userName, email, mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button logoutBtn = findViewById(R.id.profileSettings_logout);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = mAuth.getCurrentUser().getUid();
        userName = findViewById(R.id.User_Rname);
        email = findViewById(R.id.User_Remail);
        mobileNumber = findViewById(R.id.User_mobile);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Map the data to your custom HelperClass or directly retrieve the fields
                HelperClass userProfile = dataSnapshot.getValue(HelperClass.class);

                if (userProfile != null) {
                    // Get the username
                    String username = userProfile.getUsername(); // Assuming "username" field exists
                    String Email = userProfile.getEmail();
                    String mobile = userProfile.getMobile();

                    // Set the retrieved username to the TextView
                    if(username != null){
                        userName.setText(username);
                    }
                    if (Email != null){
                        email.setText(Email);
                    }
                    if (mobile != null) {
                        // If the mobile number exists, set it in the TextView
                        mobileNumber.setText(mobile);
                    } else {
                        mobileNumber.setText("xxxxxxxxxx");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileSettings.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(ProfileSettings.this, Signin.class);
                startActivity(intent);
                finish();
                Toast.makeText(ProfileSettings.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoToChangeEmail(View view) {
        startActivity(new Intent(this, ChangeEmail.class));
    }

    public void GoTOChangeName(View view) {
        startActivity(new Intent(this, ChangeName.class));
    }

    public void GoToChangeNumber(View view) {
        startActivity(new Intent(this, ChangeNumber.class));
    }

    public void GoToChnagePassword(View view) {
        startActivity(new Intent(this, ChangePassword.class));
    }

    public void GobackToUserProfile(View view) {
        startActivity(new Intent(this, UserHome.class));
    }

    public void GoToTheAddProfilePicture(View view) {
        startActivity(new Intent(this, ProfilePictureAdd.class));
    }

    public void GoToDeleteAcc(View view) {
        startActivity(new Intent(this, DeleteAccount.class));
    }
}