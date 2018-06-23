package ru.develop_for_android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import ru.develop_for_android.bakingapp.database.RecipeEntry;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<RecipeEntry> recipes;
    private Context context;
    private RecipeClickListener listener;
    private String[] localImages = new String[] {
            "bake1.jpg",
            "bake2.jpg",
            "bake3.jpg",
            "bake4.jpg",
            "bake5.jpg"
    };
    private Random randomManager;

    RecipeAdapter(Context context, RecipeClickListener listener) {
        this.context = context;
        this.listener = listener;
        randomManager = new Random();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recipe,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeEntry recipe = recipes.get(position);
        String imageAddress = recipe.getImageAddress();

        holder.recipeTitle.setText(recipe.getName());
        if (imageAddress == null || imageAddress.equals("")) {
            int randomIndex = randomManager.nextInt(5);
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/" + localImages[randomIndex]))
                    .into(holder.recipeImage);
        } else {
            Glide.with(context).load(imageAddress).into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        } else {
            return recipes.size();
        }
    }

    public void setRecipes(List<RecipeEntry> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
        Log.i("Adapter", "updated recipes with size " + recipes.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final ImageView recipeImage;
        private final TextView recipeTitle;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(recipes.get(getAdapterPosition()).getId(),
                            recipeTitle.getText().toString());
                }
            });
            recipeImage = view.findViewById(R.id.recipe_image);
            recipeTitle = view.findViewById(R.id.recipe_title);
        }
    }
}
