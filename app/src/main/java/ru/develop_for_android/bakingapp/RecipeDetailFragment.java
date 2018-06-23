package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.develop_for_android.bakingapp.database.CookingStepEntry;
import ru.develop_for_android.bakingapp.database.IngredientEntry;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment {

    private CookingStepAdapter cookingStepAdapter;
    private IngredientsAdapter ingredientsAdapter;

    public static final String RECIPE_ID_KEY = "recipe_id";
    public static final String RECIPE_TITLE_KEY = "title";

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        RecyclerView stepsList = view.findViewById(R.id.steps_list);
        stepsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        CookingStepClickListener listener;
        if (requireActivity() instanceof CookingStepClickListener) {
            listener = (CookingStepClickListener) requireActivity();
        } else {
            throw new RuntimeException("parent activity must support CookingStepClickListener");
        }
        cookingStepAdapter = new CookingStepAdapter(requireContext(), listener);
        stepsList.setAdapter(cookingStepAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(), VERTICAL);
        stepsList.addItemDecoration(decoration);
        stepsList.setNestedScrollingEnabled(false);

        RecyclerView ingredientsList = view.findViewById(R.id.ingredients_list);
        ingredientsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        ingredientsAdapter = new IngredientsAdapter(requireContext());
        ingredientsList.setAdapter(ingredientsAdapter);

        setupViewModel();

        return view;
    }

    private void setupViewModel() {
        RecipeDetailsViewModel viewModel = ViewModelProviders.of(requireActivity())
                .get(RecipeDetailsViewModel.class);
        viewModel.getSteps().observe(this, new Observer<List<CookingStepEntry>>() {
            @Override
            public void onChanged(@Nullable List<CookingStepEntry> stepEntries) {
                cookingStepAdapter.setSteps(stepEntries);
            }
        });
        viewModel.getIngredients().observe(this, new Observer<List<IngredientEntry>>() {
            @Override
            public void onChanged(@Nullable List<IngredientEntry> ingredientEntries) {
                ingredientsAdapter.setIngredients(ingredientEntries);
            }
        });
    }
}
