package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileSettings extends AppCompatActivity {

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
}