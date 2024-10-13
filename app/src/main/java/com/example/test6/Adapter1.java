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

// IM/2021/059 (Start)
// this adpter does not used here because another adapter created for the recycle view
// create a class for adapter 1

public class Adapter1 extends BaseAdapter {
    // initialize the variables and objects
    protected FirebaseAuth mAuth;
    private ArrayList<addRecipeClass> addRecipeList;
    private Context context;
    LayoutInflater layoutInflater;

    public Adapter1(ArrayList<addRecipeClass> addRecipeList, Context context) {                     // create a constructor for adapter 1
        this.addRecipeList = addRecipeList;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();                                                         // initialize firebase authentication
    }

    @Override
    public int getCount() {
        return addRecipeList.size();                                                                // return the size of the addrecipeclass
    }

    public void searchDatalist(ArrayList<addRecipeClass> searchList) {                              //  updates the list of recipes when performing a search
    this.addRecipeList = searchList;
    notifyDataSetChanged();                                                                         // update the view
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

        if (layoutInflater == null){                                                                // set content
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = layoutInflater.inflate(R.layout.grid_item, null);
        }


        String currentUserId = mAuth.getCurrentUser().getUid();                                     // get the current User Id
        String recipeOwnerId = addRecipeList.get(i).getUserId();                                    // Get the user ID of the recipe's creator

        view.setOnClickListener(new View.OnClickListener() {                                        // when user onclick go should go to the ViewEditDelete.java
            @Override
            public void onClick(View view) {
                Intent intent;
                if (currentUserId.equals(recipeOwnerId)) {                                          // if the current user is equal to the recipe owner then his recipes will show here
                    intent = new Intent(context, ViewEditDeleteRecipe.class);                       // If the current user is the owner of the recipe, go to EditRecipeView
                } else {
                    intent = new Intent(context, RecipeView.class);                                 // Otherwise, go to RecipeView
                }
                intent.putExtra("recipeId", addRecipeList.get(i).getRecipeID());              // send recipe id through intent for the relevant activity
                context.startActivity(intent);
            }
        });

                                                                                                    // Bind Data to the Views:
        ImageView gridImage = view.findViewById(R.id.gridViewImage);                                // for the grid view
        TextView videoDuration = view.findViewById(R.id.videoDuration1);                            // for the duration
        TextView recipeName = view.findViewById(R.id.recipeNameText);                               // for the recipe name

        addRecipeClass recipe = addRecipeList.get(i);
        Glide.with(context).load(addRecipeList.get(i).getImgUrl()).into(gridImage);                 // render image
        videoDuration.setText(addRecipeList.get(i).getDuration());
        recipeName.setText(addRecipeList.get(i).getName());


        return view;
    }
    // IM/2021/059 (end)
}
