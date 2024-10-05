package com.example.test6;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

public class Adapter1 extends BaseAdapter {
    protected FirebaseAuth mAuth;
    private ArrayList<addRecipeClass> addRecipeList;
    private Context context;
    LayoutInflater layoutInflater;


    public Adapter1(ArrayList<addRecipeClass> addRecipeList, Context context) {
        this.addRecipeList = addRecipeList;
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int getCount() {
        return addRecipeList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null){
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = layoutInflater.inflate(R.layout.grid_item, null);
        }

        String currentUserId = mAuth.getCurrentUser().getUid();
        // Get the user ID of the recipe's creator
        String recipeOwnerId = addRecipeList.get(i).getUserId();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (currentUserId.equals(recipeOwnerId)) {
                    // If the current user is the owner of the recipe, go to EditRecipeView
                    intent = new Intent(context, ViewEditDeleteRecipe.class);
                } else {
                    // Otherwise, go to RecipeView
                    intent = new Intent(context, RecipeView.class);
                }
                intent.putExtra("recipeId", addRecipeList.get(i).getRecipeID());
                context.startActivity(intent);
            }
        });

        ImageView gridImage = view.findViewById(R.id.gridViewImage);
        TextView videoDuration = view.findViewById(R.id.videoDuration1);
        TextView recipeName = view.findViewById(R.id.recipeNameText);

        addRecipeClass recipe = addRecipeList.get(i);
        Glide.with(context).load(addRecipeList.get(i).getImgUrl()).into(gridImage);
        videoDuration.setText(addRecipeList.get(i).getDuration());
        recipeName.setText(addRecipeList.get(i).getName());


        return view;
    }
}
