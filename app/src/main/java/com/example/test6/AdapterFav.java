package com.example.test6;

import static android.content.ContentValues.TAG;
//IM/2021/059 start
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.data.SingleRefDataBufferIterator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterFav extends RecyclerView.Adapter<AdapterFav.HolderRecipeFav>{                   // create a adapter for retriving favorite recipes to the favorite activity

    private Context context;                                                                        // Initialize the objects for the adapter
    private ArrayList<addRecipeClass> addRecipeClassArrayList;
    private static final String TAG = "FAV_BOOK_TAG";

                                                                                                    // constructor for the adapter
    public AdapterFav(Context context, ArrayList<addRecipeClass> addRecipeClassArrayList) {
        this.context = context;
        this.addRecipeClassArrayList = addRecipeClassArrayList;
    }

    @NonNull
    @Override                                                                                       // on create the bind the data
    public HolderRecipeFav onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new HolderRecipeFav(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecipeFav holder, int position) {
                                                                                                    // get the recipes from the recipe class
        addRecipeClass addRecipeClass = addRecipeClassArrayList.get(position);
        Glide.with(context).load(addRecipeClassArrayList.get(position).getImgUrl()).into(holder.recycleImage);
        loadRecipeDetails(addRecipeClass, holder);                                                  // load recipes from the class to the holder

                                                                                                    // handle click, open the recipe details from the viewEditDeleteRecipe.java
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewEditDeleteRecipe.class);
                intent.putExtra("recipeId", addRecipeClass.getRecipeID());                    // pass the recipeId
                context.startActivity(intent);
            }
        });

    }

    private void loadRecipeDetails(addRecipeClass addRecipeClass, HolderRecipeFav holder) {         // load the recipes details in to the card view
        String recipeId = addRecipeClass.getRecipeID();                                             // initialize the variables
        Log.d(TAG, "LoadRecipeDetails: Recipe Details of Recipe ID" + recipeId );

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("recipes");  // get the database reference and load the data from the database
        reference.child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {         // send the recipe id and load the data
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                    // get recipe info
                String recipeName = ""+dataSnapshot.child("name").getValue();                  // recipe name
                String recipeDuration = ""+dataSnapshot.child("duration").getValue();          // recipe duration
                String recipeImgUrl = ""+dataSnapshot.child("imgUrl").getValue();              // recipe image url

                addRecipeClass.setFavorite(true);                                                   // set data to the items
                addRecipeClass.setName(recipeName);
                addRecipeClass.setDuration(recipeDuration);
                addRecipeClass.setImgUrl(recipeImgUrl);

                holder.recycleText.setText(recipeName);
                holder.recycleDuration.setText(recipeDuration);

                                                                                                    // Load the image
                Glide.with(context).load(recipeImgUrl).into(holder.recycleImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return addRecipeClassArrayList.size();                                                      // return list size of records
    }

    public void searchDatalist(ArrayList<addRecipeClass> searchList) {                              // get the search result
        this.addRecipeClassArrayList = searchList;
        notifyDataSetChanged();
    }

    class HolderRecipeFav extends RecyclerView.ViewHolder{                                          // bind values to the recycle view

        ImageView recycleImage;
        TextView recycleText, recycleDuration;
        CardView recycleCard;

        public HolderRecipeFav(@NonNull View itemView) {
            super(itemView);

            recycleImage = itemView.findViewById(R.id.recycleImage);
            recycleText = itemView.findViewById(R.id.recycleRecipeNameText);
            recycleDuration = itemView.findViewById(R.id.recycleVideoDuration1);
            recycleCard = itemView.findViewById(R.id.recycleCard);
        }
    }
}

//IM/2021/059 End