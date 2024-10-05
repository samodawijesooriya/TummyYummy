package com.example.test6;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// IM/2021/082 (Start)

public class ProfilePictureAdd extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    private ImageView uploadImage;
    ProgressBar progressBar;
    private Uri imageUri;
    Button save;
    private String userId;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_picture_add);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        uploadImage = findViewById(R.id.ProfilePictureAdd_imageUp);
        progressBar = findViewById(R.id.Profile_progessBar);
        progressBar.setVisibility(View.INVISIBLE);
        save = findViewById(R.id.BtnSave);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == AppCompatActivity.RESULT_OK){
                    Intent data = result.getData();
                    assert data != null;
                    imageUri = data.getData();
                    uploadImage.setImageURI(imageUri);
                }else{
                    Toast.makeText(ProfilePictureAdd.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent photoPicker = new Intent();
              photoPicker.setAction(Intent.ACTION_GET_CONTENT);
              photoPicker.setType("image/*");
              activityResultLauncher.launch(photoPicker);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null){
                    progressBar.setVisibility(View.VISIBLE);
                    uploadToFirebase(imageUri);
                }else{
                    Toast.makeText(ProfilePictureAdd.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadToFirebase(Uri uri){

        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));


        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Update only imgUrl in the database
                            databaseReference.child(userId).child("imgUrl").setValue(uri.toString());
                            progressBar.setVisibility(View.INVISIBLE);
                            save.setEnabled(true); // Re-enable save button
                            Toast.makeText(ProfilePictureAdd.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProfilePictureAdd.this, UserHome.class));
                            finish();

                        }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfilePictureAdd.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    public void GoToProfileSettings(View view) {
        startActivity(new Intent(this, ProfileSettings.class));
    }
}

// IM/2021/082 (End)