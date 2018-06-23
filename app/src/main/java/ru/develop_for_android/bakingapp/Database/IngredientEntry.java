package ru.develop_for_android.bakingapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ingredient",
        indices = {@Index("recipe_id")},
        foreignKeys = @ForeignKey(entity = RecipeEntry.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE))
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
    public void setId(int id) {
        this.id = id;
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

    public String formMeasureString() {
        return String.valueOf(quantity) + " " + measure;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

}
