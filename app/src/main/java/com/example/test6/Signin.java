package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText SignInEmail, SignInPassword, SignInUsername;
    private Button SignInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();
        SignInEmail = findViewById(R.id.sign_in_email);
        SignInPassword = findViewById(R.id.sign_in_password);
        SignInBtn = findViewById(R.id.sign_in_login);

        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = SignInEmail.getText().toString();
                String password = SignInPassword.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Signin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Signin.this, Home.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Signin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        SignInPassword.setError("Password cannot be empty");
                    }
                }else if (email.isEmpty()){
                    SignInEmail.setError("Email cannot be empty");
                }else{
                    SignInEmail.setError("Please enter valid email");
                }
            }
        });
    }

    public void GoHome(View view) {
        startActivity(new Intent(this, Home.class));
    }

    public void SignUp(View view) {
        startActivity(new Intent(this, SignUp.class));
    }
}