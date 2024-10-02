package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRecipe extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText name, ingredients, method, duration;
    Button addBtn, cancelBtn;
    Spinner category;
    DatabaseReference reference;

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

        Spinner spinner = findViewById(R.id.addRecipe_dropdown_spinner);

// Get the array from the strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_category, android.R.layout.simple_spinner_item);

// Set the layout for dropdown options
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter to the spinner
        spinner.setAdapter(adapter);

// Optionally, handle the selection events
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("recipes");

        name = findViewById(R.id.addRecipe_recipeNameText);
        ingredients = findViewById(R.id.addRecipe_ingredientTextAdd);
        method = findViewById(R.id.addRecipe_methodAdd);
        duration = findViewById(R.id.addRecipe_AddPreparationTime);
        addBtn = findViewById(R.id.addRecipe_AddBtn);
        cancelBtn = findViewById(R.id.addRecipe_cancelBtn);
        category = findViewById(R.id.addRecipe_dropdown_spinner);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipeName = name.getText().toString();
                String recipeIngredients = ingredients.getText().toString();
                String recipeMethod = method.getText().toString();
                String videoDuration = duration.getText().toString();
                String selectedCategory = category.getSelectedItem().toString();


                // validation
                if (recipeName.isEmpty() || recipeIngredients.isEmpty() || recipeMethod.isEmpty() || videoDuration.isEmpty()) {
                    Snackbar.make(findViewById(R.id.main), "Recipe added successfully", Snackbar.LENGTH_SHORT).show();
                }else {
                    String recipeID = reference.push().getKey();

                    addRecipeClass recipe = new addRecipeClass(recipeID, recipeName, recipeIngredients, recipeMethod, videoDuration, selectedCategory);

                    if (recipeID != null) {
                        reference.child(recipeID).setValue(recipe)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddRecipe.this, "Recipe added successfully", Toast.LENGTH_SHORT).show();
                                        clearFields();  // Clear the input fields
                                    } else {
                                        Toast.makeText(AddRecipe.this, "Failed to add recipe", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
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