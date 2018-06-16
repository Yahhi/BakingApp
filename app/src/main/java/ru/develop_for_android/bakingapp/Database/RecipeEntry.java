package ru.develop_for_android.bakingapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "recipe")
public class RecipeEntry {
    @PrimaryKey
    private int id;
    private String name;
    private int servings;
    @ColumnInfo(name = "image_address")
    private String imageAddress;

    public RecipeEntry(int id, String name, int servings, String imageAddress) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.imageAddress = imageAddress;
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

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
