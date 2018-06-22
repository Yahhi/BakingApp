package ru.develop_for_android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.develop_for_android.bakingapp.database.IngredientEntry;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>{

    private List<IngredientEntry> ingredients;
    private Context context;

    IngredientsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientEntry ingredient = ingredients.get(position);

        holder.titleView.setText(ingredient.getName());
        holder.quantityView.setText(ingredient.formMeasureString());
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) {
            return 0;
        } else {
            return ingredients.size();
        }
    }

    public void setIngredients(List<IngredientEntry> list) {
        this.ingredients = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final TextView titleView;
        private final TextView quantityView;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titleView = view.findViewById(R.id.ingredient_title);
            quantityView = view.findViewById(R.id.ingredient_quantity);
        }
    }
}
