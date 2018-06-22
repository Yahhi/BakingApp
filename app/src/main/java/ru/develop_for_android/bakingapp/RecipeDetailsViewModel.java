package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.database.CookingStepEntry;
import ru.develop_for_android.bakingapp.database.IngredientEntry;

public class RecipeDetailsViewModel extends ViewModel {

    private LiveData<List<CookingStepEntry>> steps;
    private LiveData<List<IngredientEntry>> ingredients;

    RecipeDetailsViewModel(@NonNull AppDatabase database, int recipeId) {
        steps = database.recipeDao().loadStepsForRecipe(recipeId);
        ingredients = database.recipeDao().loadIngredientsForRecipe(recipeId);
    }

    public LiveData<List<CookingStepEntry>> getSteps() {
        return steps;
    }

    public LiveData<List<IngredientEntry>> getIngredients() {
        return ingredients;
    }
}
