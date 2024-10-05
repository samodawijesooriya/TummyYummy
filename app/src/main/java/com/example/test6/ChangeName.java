package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeName extends AppCompatActivity {

    private EditText Name;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        Name = findViewById(R.id.editTextTextFirstName);
    }

    public void ChangeNameFunc(View view) {
        String newName = Name.getText().toString().trim();

        // Validate input
        if (newName.isEmpty()) {
            Name.setError("Name cannot be empty");
            Name.requestFocus();
            return;
        }

        // Get current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Update Firebase with the new name
        reference.child(userId).child("username").setValue(newName).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Success feedback to the user
                        Toast.makeText(ChangeName.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, ProfileSettings.class));
                    } else {
                        // Failed to update
                        Toast.makeText(ChangeName.this, "Failed to update name", Toast.LENGTH_SHORT).show();
                    }
        });
    }


}