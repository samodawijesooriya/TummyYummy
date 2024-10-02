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

import java.util.Objects;

public class ProfileSettings extends AppCompatActivity {

    protected FirebaseAuth mAuth;

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