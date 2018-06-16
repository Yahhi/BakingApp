package ru.develop_for_android.bakingapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredient",
        foreignKeys = @ForeignKey(entity = RecipeEntry.class,
                parentColumns = "id",
                childColumns = "recipe_id"))
public class IngredientEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String measure;
    private double quantity;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    public IngredientEntry(int recipeId, String name, String measure, double quantity) {
        this.recipeId = recipeId;
        this.name = name;
        this.measure = measure;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setId(int id) {
        this.id = id;
    }
}
