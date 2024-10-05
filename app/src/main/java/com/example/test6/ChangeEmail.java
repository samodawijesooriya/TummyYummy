package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

// IM/2021/082 (Start)

public class ChangeEmail extends AppCompatActivity {

    private EditText Email;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        Email = findViewById(R.id.change_email_editTextTextEmailAddress);
    }

    public void ChangeEmailFunc(View view) {
        String newEmail = Email.getText().toString().trim();

        // validations
        if (newEmail.isEmpty()) {
            Email.setError("Name cannot be empty");
            Email.requestFocus();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        reference.child(userId).child("email").setValue(newEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ChangeEmail.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ProfileSettings.class));
            } else {
                Toast.makeText(ChangeEmail.this, "Failed to update email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

// IM/2021/082 (End)