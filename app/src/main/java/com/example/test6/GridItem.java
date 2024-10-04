package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GridItem extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.myLinearLayout), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
        });
    }

    // This method is triggered when the ImageView is clicked
    public void GoToRecipeView(View view) {
        // Navigate to another activity
        Intent intent = new Intent(GridItem.this, History.class);
        startActivity(intent);
    }
}
