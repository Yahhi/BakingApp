package ru.develop_for_android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe ORDER BY name")
    LiveData<List<RecipeEntry>> loadAllRecipes();

    @Insert
    void insertRecipe(RecipeEntry recipe);

    @Query("SELECT * FROM ingredient WHERE recipe_id = :id")
    LiveData<List<IngredientEntry>> loadIngredientsForRecipe(int id);

    @Insert
    void insertIngredient(IngredientEntry ingredient);

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId ORDER BY order_id ASC")
    LiveData<List<CookingStepEntry>> loadStepsForRecipe(int recipeId);

    @Query("SELECT * FROM step WHERE id = :id")
    LiveData<CookingStepEntry> loadStepById(int id);

    @Insert
    void insertStep(CookingStepEntry step);

    @Query("DELETE FROM recipe")
    void clearRecipes();

}
