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

public class ChangeNumber extends AppCompatActivity {

    private EditText Number;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_number);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        Number = findViewById(R.id.change_number_editTextPhone);
    }

    public void ChangeNumberFunc(View view) {
        String newNumber = Number.getText().toString().trim();

        // Validations
        if (newNumber.isEmpty()) {
            Number.setError("Number cannot be empty");
            Number.requestFocus();
            return;
        } else if (newNumber.length() != 10) {
            Number.setError("Number must be 10 digits long");
            Number.requestFocus();
            return;
        } else if (!newNumber.matches("\\d+")) {
            Number.setError("Number must contain only digits");
            Number.requestFocus();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        reference.child(userId).child("mobile").setValue(newNumber).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ChangeNumber.this, "Mobile Number updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ProfileSettings.class));
            } else {
                Toast.makeText(ChangeNumber.this, "Failed to update mobile number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoToUserProfilefromChangeNumber(View view) {
        startActivity(new Intent(this, ProfileSettings.class));
    }
}

// IM/2021/082 (End)