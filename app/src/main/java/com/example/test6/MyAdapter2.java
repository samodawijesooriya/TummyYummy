package com.example.test6;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    // IM/2021/059 (Start)
    // create firebase object
    protected FirebaseAuth mAuth;
    private Context context;
    private List<addRecipeClass> addRecipeClassList;

    // MayAdapter2 constructor
    public MyAdapter2(Context context, List<addRecipeClass> addRecipeClassList) {
        this.context = context;
        this.addRecipeClassList = addRecipeClassList;
        mAuth = FirebaseAuth.getInstance();
    }

    // Get the recycle items
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new MyViewHolder(view);
    }

    // fetch data for the recycle view
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(addRecipeClassList.get(position).getImgUrl()).into(holder.recycleImage);       // set the image for the cardview
        holder.recycleText.setText(addRecipeClassList.get(position).getName());                                 // set the textView for the cardView
        holder.recycleDuration.setText(addRecipeClassList.get(position).getDuration());                         // set the duration for thr cardView

        String RecipeID = addRecipeClassList.get(position).getRecipeID();                                       // get recipe Id
        String currentUserId = mAuth.getCurrentUser().getUid();                                                 // get current userId
        String recipeOwnerId = addRecipeClassList.get(position).getUserId();                                    // get recipe ownerId

        // holder calling for when clicking the card
        holder.recycleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                // check if the current user can edit or view the recipes
                if (currentUserId.equals(recipeOwnerId)) {
                    // If the current user is the owner of the recipe, go to EditRecipeView
                    intent = new Intent(context, ViewEditDeleteRecipe.class);
                } else {
                    // Otherwise, go to RecipeView
                    intent = new Intent(context, RecipeView.class);
                }
                // send the recipe Id for the category
                intent.putExtra("recipeId", RecipeID);
                context.startActivity(intent);
            }
        });
    }

    // get item sizes for the search view
    @Override
    public int getItemCount() {
        return addRecipeClassList.size();
    }

    // get item sizes for the search view
    public void searchDatalist(ArrayList<addRecipeClass> searchList) {
        this.addRecipeClassList = searchList;
        notifyDataSetChanged();
    }


    // recycle view constructor
    class MyViewHolder extends RecyclerView.ViewHolder {

        // define the varibles
        ImageView recycleImage;
        TextView recycleText, recycleDuration;
        CardView recycleCard;

        public MyViewHolder(@NonNull View itemView) {
            // get items
            super(itemView);
            recycleImage = itemView.findViewById(R.id.recycleImage);
            recycleText = itemView.findViewById(R.id.recycleRecipeNameText);
            recycleDuration = itemView.findViewById(R.id.recycleVideoDuration1);
            recycleCard = itemView.findViewById(R.id.recycleCard);
        }
    }
    // IM/2021/059 (End)
}