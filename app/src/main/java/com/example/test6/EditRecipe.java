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

public class EditRecipe extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String recipeId;
    private EditText recipeName;
    private EditText ingredientsEdit;
    private EditText methodEdit;
    private EditText durationEdit;
    Button saveBtn, editCancelBtn;
    private Uri imageUri;
    private ImageView uploadImg;
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

        // Initialize UI components
        recipeName = findViewById(R.id.editRecipe_recipeNameText);
        ingredientsEdit = findViewById(R.id.editRecipe_ingredientTextAdd);
        methodEdit = findViewById(R.id.addRecipe_methodAdd);
        durationEdit = findViewById(R.id.addRecipe_AddPreparationTime);
        saveBtn = findViewById(R.id.editRecipe_SaveBtn);
        editCancelBtn = findViewById(R.id.editRecipe_CancelBtn);

        Spinner category = findViewById(R.id.editRecipe_dropdown_spinner);

        uploadImg = findViewById(R.id.EdituploadImage);
        progressBar = findViewById(R.id.EditprogressBarimg);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("recipes");

        recipeId = getIntent().getStringExtra("recipeId");

        // Get data from the database and fetch them inside of the text boxes
        reference.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addRecipeClass addRecipeClass = dataSnapshot.getValue(addRecipeClass.class);

                if(addRecipeClass != null){
                    String name = addRecipeClass.getName();
                    String ingredients = addRecipeClass.getIngredients();
                    String method = addRecipeClass.getMethod();
                    String duration = addRecipeClass.getDuration();

                    recipeName.setText(name);
                    ingredientsEdit.setText(ingredients);
                    methodEdit.setText(method);
                    durationEdit.setText(duration);

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

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // When click save button save to the database
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = recipeName.getText().toString();
                String recipeIngredients = ingredientsEdit.getText().toString();
                String recipeMethod = methodEdit.getText().toString();
                String videoDuration = durationEdit.getText().toString();
                String selectedCategory = category.getSelectedItem().toString();

                // validation
                if (name.isEmpty() || recipeIngredients.isEmpty() || recipeMethod.isEmpty() || videoDuration.isEmpty()) {
                    Snackbar.make(findViewById(R.id.main), "Please fill all fields", Snackbar.LENGTH_SHORT).show();
                } else {
                    String userID = mAuth.getCurrentUser().getUid();

                    // Update the existing recipe using the recipeId
                    addRecipeClass updatedRecipe = new addRecipeClass(recipeId, name, recipeIngredients, recipeMethod, videoDuration, selectedCategory, userID);

                    if(imageUri != null){
                        progressBar.setVisibility(View.VISIBLE);
                        uploadToFirebase(imageUri, recipeId);
                    }else{
                        Toast.makeText(EditRecipe.this, "Please select image", Toast.LENGTH_SHORT).show();
                    }

                    reference.child(recipeId).setValue(updatedRecipe).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditRecipe.this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                            clearFields();  // Clear the input fields or navigate back if needed
                        } else {
                            Toast.makeText(EditRecipe.this, "Failed to update recipe", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


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
                // Handle selection
                String selectedCategory = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do something when nothing selected
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
                                Toast.makeText(EditRecipe.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditRecipe.this, Category.class));
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