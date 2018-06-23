package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.database.CookingStepEntry;
import ru.develop_for_android.bakingapp.database.IngredientEntry;

public class RecipeDetailsViewModel extends ViewModel {

    private LiveData<List<CookingStepEntry>> steps;
    private LiveData<List<IngredientEntry>> ingredients;

    private LiveData<CookingStepEntry> stepEntry;


    private AppDatabase database;

    RecipeDetailsViewModel(@NonNull AppDatabase database, int recipeId) {
        Log.i("LIVE", "recipe_id is set to " + recipeId);
        this.database = database;

        steps = database.recipeDao().loadStepsForRecipe(recipeId);
        ingredients = database.recipeDao().loadIngredientsForRecipe(recipeId);
    }

    public LiveData<List<CookingStepEntry>> getSteps() {
        return steps;
    }

    public LiveData<List<IngredientEntry>> getIngredients() {
        return ingredients;
    }

    public void selectStep(int stepId) {
        stepEntry = database.recipeDao().loadStepById(stepId);
    }
    public LiveData<CookingStepEntry> getStepEntry() {
        return stepEntry;
    }
}
