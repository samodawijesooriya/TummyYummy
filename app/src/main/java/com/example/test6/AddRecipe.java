package com.example.test6;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// IM/2021/104 Start
public class AddRecipe extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String recipeId;
    EditText name, ingredients, method, duration;
    Button addBtn, cancelBtn;
    Spinner category;
    private Uri imageUri;
    private Uri videoUri;
    DatabaseReference reference;
    private FloatingActionButton uploadImgBtn;
    private ImageView uploadImg, uploadVideo;
    ProgressBar progressBar;
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner category = findViewById(R.id.addRecipe_dropdown_spinner);

        // Get the array from the strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_category, android.R.layout.simple_spinner_item);

        // Set the layout for dropdown options
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        category.setAdapter(adapter);

        // Optionally, handle the selection events
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        mAuth = FirebaseAuth.getInstance();
        recipeId = mAuth.getCurrentUser().getUid();
        uploadImg = findViewById(R.id.uploadImage);
        progressBar = findViewById(R.id.progressBarimg);
        progressBar.setVisibility(View.INVISIBLE);
        uploadVideo = findViewById(R.id.addRecipe_uploadVideo);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("recipes");


        name = findViewById(R.id.addRecipe_recipeNameText);
        ingredients = findViewById(R.id.addRecipe_ingredientTextAdd);
        method = findViewById(R.id.addRecipe_methodAdd);
        duration = findViewById(R.id.addRecipe_AddPreparationTime);
        addBtn = findViewById(R.id.addRecipe_AddBtn);
        cancelBtn = findViewById(R.id.addRecipe_cancelBtn);
//      category = findViewById(R.id.addRecipe_dropdown_spinner);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == AppCompatActivity.RESULT_OK){
                    Intent data = result.getData();
                    assert data != null;
                    imageUri = data.getData();
                    uploadImg.setImageURI(imageUri);

                }else{
                    Toast.makeText(AddRecipe.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ActivityResultLauncher<Intent> videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                videoUri = data.getData();
                                Glide.with(AddRecipe.this).load(videoUri).into(uploadVideo);  // Update the video view
                            } else {
                                Toast.makeText(AddRecipe.this, "No video selected", Toast.LENGTH_SHORT).show();
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


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipeName = name.getText().toString();
                String recipeIngredients = ingredients.getText().toString();
                String recipeMethod = method.getText().toString();
                String videoDuration = duration.getText().toString().replaceAll("\\D", "") + " min";
                String selectedCategory = category.getSelectedItem().toString();


                // validation
                if (recipeName.isEmpty() || recipeIngredients.isEmpty() || recipeMethod.isEmpty() || videoDuration.isEmpty()) {
                    Snackbar.make(findViewById(R.id.main), "Recipe added successfully", Snackbar.LENGTH_SHORT).show();
                }else {
                    String recipeID = reference.push().getKey();
                    String userID = mAuth.getCurrentUser().getUid();

                    addRecipeClass recipe = new addRecipeClass(recipeID, recipeName, recipeIngredients, recipeMethod, videoDuration, selectedCategory, userID);

                    if(imageUri != null || videoUri != null){
                        progressBar.setVisibility(View.VISIBLE);
                        uploadToFirebase(imageUri, recipeID);
                        uploadVideo(videoUri, recipeID);
                    }else{
                        Toast.makeText(AddRecipe.this, "Please select image and video", Toast.LENGTH_SHORT).show();
                    }


                    if (recipeID != null) {
                        reference.child(recipeID).setValue(recipe).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddRecipe.this, "Recipe added successfully", Toast.LENGTH_SHORT).show();
                                clearFields();  // Clear the input fields
                                startActivity(new Intent(AddRecipe.this, MyRecipe.class));  // Go back to recipe list
                                finish();
                            } else {
                                Toast.makeText(AddRecipe.this, "Failed to add recipe", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });



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
                        startActivity(new Intent(AddRecipe.this, MyRecipe.class));
                        finish();
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
                Toast.makeText(AddRecipe.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
                                startActivity(new Intent(AddRecipe.this, MyRecipe.class));
                                finish();
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
                        Toast.makeText(AddRecipe.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFields() {
        name.setText("");
        ingredients.setText("");
        method.setText("");
        duration.setText("");
    }

    public void GoBackToMyRecipe(View view) {
        startActivity(new Intent(this, MyRecipe.class));
    }
}

// IM/2021/104 End