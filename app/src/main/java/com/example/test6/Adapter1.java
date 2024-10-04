package com.example.test6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

public class Adapter1 extends BaseAdapter {

    private ArrayList<addRecipeClass> addRecipeList;
    private Context context;
    LayoutInflater layoutInflater;


    public Adapter1(ArrayList<addRecipeClass> addRecipeList, Context context) {
        this.addRecipeList = addRecipeList;
        this.context = context;
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
        ImageView gridImage = view.findViewById(R.id.gridViewImage);
        TextView videoDuration = view.findViewById(R.id.videoDuration1);
        TextView recipeName = view.findViewById(R.id.recipeNameText);

        Glide.with(context).load(addRecipeList.get(i).getImgUrl()).into(gridImage);
        videoDuration.setText(addRecipeList.get(i).getDuration());
        recipeName.setText(addRecipeList.get(i).getName());


        return view;
    }
}
