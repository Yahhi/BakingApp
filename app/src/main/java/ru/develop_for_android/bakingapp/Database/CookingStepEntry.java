package ru.develop_for_android.bakingapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "step",
        foreignKeys = @ForeignKey(entity = RecipeEntry.class,
                parentColumns = "id",
                childColumns = "recipe_id"))
public class CookingStepEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "order_id")
    private int orderingId;
    @ColumnInfo(name = "short_description")
    private String shortDescription;
    private String description;
    @ColumnInfo(name = "video_url")
    private String videoUrl;
    @ColumnInfo(name = "thumbnail_url")
    private String thumbnailUrl;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    public CookingStepEntry(int id, String shortDescription, String description, String videoUrl,
                            String thumbnailUrl, int recipeId) {
        this.orderingId = id;
        this.description = description;
        this.shortDescription = shortDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getOrderingId() {
        return orderingId;
    }

    public void setOrderingId(int orderingId) {
        this.orderingId = orderingId;
    }
}
