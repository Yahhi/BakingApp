package ru.develop_for_android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class RecipeDao {

    @Query("SELECT * FROM recipe ORDER BY name")
    public abstract LiveData<List<RecipeEntry>> loadAllRecipes();

    @Query("SELECT COUNT(*) AS recipesCount FROM recipe")
    public abstract int getRecipesCount();

    @Insert
    public abstract void insertRecipes(RecipeEntry[] recipes);

    @Query("SELECT * FROM ingredient WHERE recipe_id = :id")
    public abstract LiveData<List<IngredientEntry>> loadIngredientsForRecipe(int id);

    @Query("SELECT * FROM ingredient WHERE recipe_id = :id")
    public abstract List<IngredientEntry> loadFinalIngredientsForRecipe(int id);
    @Query("SELECT name FROM recipe WHERE id = :id")
    public abstract String loadFinalRecipeName(int id);

    @Insert
    public abstract void insertIngredients(ArrayList<IngredientEntry> ingredient);

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId ORDER BY order_id ASC")
    public abstract LiveData<List<CookingStepEntry>> loadStepsForRecipe(int recipeId);

    @Insert
    public abstract void insertSteps(ArrayList<CookingStepEntry> step);

    @Query("DELETE FROM recipe")
    public abstract void clearRecipes();

    @Transaction
    public void insertJsonResults(RecipeEntry[] recipeEntries,
                           ArrayList<IngredientEntry> ingredientEntries,
                           ArrayList<CookingStepEntry> stepEntries) {
        insertRecipes(recipeEntries);
        insertIngredients(ingredientEntries);
        insertSteps(stepEntries);
    }
}
