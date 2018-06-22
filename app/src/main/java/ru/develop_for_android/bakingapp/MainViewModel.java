package ru.develop_for_android.bakingapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.database.RecipeEntry;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<RecipeEntry>> recipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        recipes = database.recipeDao().loadAllRecipes();
    }

    public LiveData<List<RecipeEntry>> getRecipes() {
        return recipes;
    }
}
