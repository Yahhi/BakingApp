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

import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.database.CookingStepEntry;
import ru.develop_for_android.bakingapp.database.IngredientEntry;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment {

    private StepAdapter stepAdapter;
    private IngredientsAdapter ingredientsAdapter;

    public static final String RECIPE_ID_KEY = "recipe_id";
    private int recipeId;

    public static RecipeDetailFragment newInstance(int recipeId) {

        Bundle args = new Bundle();
        args.putInt(RECIPE_ID_KEY, recipeId);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(RECIPE_ID_KEY)) {
            recipeId = args.getInt(RECIPE_ID_KEY);
        }
    }

    public RecipeDetailFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        RecyclerView stepsList = view.findViewById(R.id.steps_list);
        stepsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        stepAdapter = new StepAdapter(requireContext());
        stepsList.setAdapter(stepAdapter);
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
        RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(
                AppDatabase.getInstance(requireContext()), recipeId);
        RecipeDetailsViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(RecipeDetailsViewModel.class);
        viewModel.getSteps().observe(this, new Observer<List<CookingStepEntry>>() {
            @Override
            public void onChanged(@Nullable List<CookingStepEntry> stepEntries) {
                stepAdapter.setSteps(stepEntries);
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
