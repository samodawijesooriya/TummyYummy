package com.example.test6;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                                                                                                                    // IM/2021/082 (Start)
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

        ImageView forgotPass = findViewById(R.id.arrow_ChangePassword);
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
                HelperClass userProfile = dataSnapshot.getValue(HelperClass.class);                                 // Map the data to custom HelperClass or directly retrieve the fields

                if (userProfile != null) {
                    String username = userProfile.getUsername();                                                    // get Details
                    String Email = userProfile.getEmail();
                    String mobile = userProfile.getMobile();

                    if(username != null){
                        userName.setText(username);
                    }
                    if (Email != null){
                        email.setText(Email);
                    }
                    if (mobile != null) {
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

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettings.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_fogot, null);
                EditText emailBox = dialogView.findViewById(R.id.dialogFogot_emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.dialogFogot_btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();

                        if(TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(ProfileSettings.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ProfileSettings.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(ProfileSettings.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.dialogFogot_btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
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

    public void GobackToUserProfile(View view) {
        startActivity(new Intent(this, UserHome.class));
    }

    public void GoToTheAddProfilePicture(View view) {
        startActivity(new Intent(this, ProfilePictureAdd.class));
    }

    public void GoToDeleteAcc(View view) {
        startActivity(new Intent(this, DeleteAccount.class));
    }
}                                                                                                                   // IM/2021/082 (End)