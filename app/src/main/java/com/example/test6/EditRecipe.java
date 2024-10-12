package com.example.test6;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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

public class EditRecipe extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String recipeId, existingImage, existingVideo;
    private EditText recipeName;
    private EditText ingredientsEdit;
    private EditText methodEdit;
    private EditText durationEdit;
    Button saveBtn, editCancelBtn;
    private Uri imageUri,videoUriEdit;
    private ImageView uploadImg, uploadVideo;
    ProgressBar progressBar;
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializing Variables
        recipeName = findViewById(R.id.editRecipe_recipeNameText);
        ingredientsEdit = findViewById(R.id.editRecipe_ingredientTextAdd);
        methodEdit = findViewById(R.id.addRecipe_methodAdd);
        durationEdit = findViewById(R.id.addRecipe_AddPreparationTime);
        saveBtn = findViewById(R.id.editRecipe_SaveBtn);
        editCancelBtn = findViewById(R.id.editRecipe_CancelBtn);

        Spinner category = findViewById(R.id.editRecipe_dropdown_spinner);

        uploadImg = findViewById(R.id.EdituploadImage);
        uploadVideo = findViewById(R.id.EdituploadVideo);
        progressBar = findViewById(R.id.EditprogressBarimg);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("recipes");

        recipeId = getIntent().getStringExtra("recipeId");

        // Get data from DB
        reference.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addRecipeClass addRecipeClass = dataSnapshot.getValue(addRecipeClass.class);

                if(addRecipeClass != null){
                    String name = addRecipeClass.getName();
                    String ingredients = addRecipeClass.getIngredients();
                    String method = addRecipeClass.getMethod();
                    String duration = addRecipeClass.getDuration();
                    String imgUrl = addRecipeClass.getImgUrl();
                    String videoUrl = addRecipeClass.getVideoUrl();

                    // Retrieve Data FRom FireBase
                    recipeName.setText(name);
                    ingredientsEdit.setText(ingredients);
                    methodEdit.setText(method);
                    durationEdit.setText(duration);

                    existingImage = imgUrl;
                    existingVideo = videoUrl;

                    // Load existing image thumbnail into imageView
                    if (imgUrl != null && !imgUrl.isEmpty()) {
                        Glide.with(EditRecipe.this).load(imgUrl).into(uploadImg);
                    }
                    // Load existing video thumbnail into VideoView
                    if (videoUrl != null && !videoUrl.isEmpty()) {
                        Glide.with(EditRecipe.this).load(videoUrl).into(uploadVideo); // Shows the thumbnail
                    }


                }else{
                    Toast.makeText(EditRecipe.this,"No data", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditRecipe.this,"Database Error", Toast.LENGTH_LONG).show();
            }
        });

        // Upload image code
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == AppCompatActivity.RESULT_OK){
                    Intent data = result.getData();
                    assert data != null;
                    imageUri = data.getData();
                    uploadImg.setImageURI(imageUri);

                }else{
                    Toast.makeText(EditRecipe.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Upload video code
        ActivityResultLauncher<Intent> videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                videoUriEdit = data.getData();
                                Glide.with(EditRecipe.this).load(videoUriEdit).into(uploadVideo);  // Update the video view
                            } else {
                                Toast.makeText(EditRecipe.this, "No video selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoPicker = new Intent(Intent.ACTION_PICK);
                videoPicker.setType("video/*");
                videoPickerLauncher.launch(videoPicker);  // Use the video picker launcher
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Save to DB
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = recipeName.getText().toString();
                String recipeIngredients = ingredientsEdit.getText().toString();
                String recipeMethod = methodEdit.getText().toString();
                String videoDuration = durationEdit.getText().toString().replaceAll("\\D", "") + " min";;
                String selectedCategory = category.getSelectedItem().toString();

                // validation
                if (name.isEmpty() || recipeIngredients.isEmpty() || recipeMethod.isEmpty() || videoDuration.isEmpty()) {
                    Snackbar.make(findViewById(R.id.main), "Please fill all fields", Snackbar.LENGTH_SHORT).show();
                } else {
                    String userID = mAuth.getCurrentUser().getUid();

                    reference.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            addRecipeClass exRecipe = dataSnapshot.getValue(addRecipeClass.class);

                            // Update the existing recipe using the recipeId
                            addRecipeClass updatedRecipe = new addRecipeClass(recipeId, name, recipeIngredients, recipeMethod, videoDuration, selectedCategory, userID);

                            // Handle image and video upload
                            if (imageUri != null) {
                                uploadToFirebase(imageUri, recipeId);  // Upload new image
                            } else{
                                updatedRecipe.setImgUrl(existingImage); //  Keep the existing image
                            }

                            if (videoUriEdit != null) {
                                uploadVideo(videoUriEdit, recipeId);  // Upload new video
                            } else{
                                updatedRecipe.setVideoUrl(existingVideo); // Keep the existing video
                            }

                            reference.child(recipeId).setValue(updatedRecipe).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditRecipe.this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                    startActivity(new Intent(EditRecipe.this, Category.class));
                                    finish();
                                } else {
                                    Toast.makeText(EditRecipe.this, "Failed to update recipe", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(EditRecipe.this, "Database Error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        editCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditRecipe.this, ViewEditDeleteRecipe.class));
                finish();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_category, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(adapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle selection
                String selectedCategory = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadVideo(Uri uri, String recipeID){
        final StorageReference videoReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        videoReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                videoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Update the imgUrl field in the database
                        reference.child(recipeID).child("videoUrl").setValue(uri.toString());
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(EditRecipe.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditRecipe.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadToFirebase(Uri uri, String recipeID) {
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        imageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Update the imgUrl field in the database
                                reference.child(recipeID).child("imgUrl").setValue(uri.toString());
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(EditRecipe.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFields() {
        recipeName.setText("");
        ingredientsEdit.setText("");
        methodEdit.setText("");
        durationEdit.setText("");
    }
}

// IM/2021/082 (End)