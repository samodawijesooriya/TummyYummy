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
    protected FirebaseUser firebaseUser;

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

        TextView deleteAcc = findViewById(R.id.profileSettings_deleteUser);
        Button logoutBtn = findViewById(R.id.profileSettings_logout);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

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

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileSettings.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your "+
                        "account from the system and you won't be able to access the app.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog progressDialog = new AlertDialog.Builder(ProfileSettings.this)
                                .setView(R.layout.dialog_progress) // Use the custom layout
                                .setCancelable(false) // Prevent cancellation
                                .create();

                        progressDialog.show();

                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(ProfileSettings.this, "Account Deleted", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ProfileSettings.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(ProfileSettings.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
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
}