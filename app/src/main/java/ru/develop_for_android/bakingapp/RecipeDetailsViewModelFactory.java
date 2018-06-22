package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ru.develop_for_android.bakingapp.database.AppDatabase;

public class RecipeDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase database;
    private final int recipeId;

    RecipeDetailsViewModelFactory(AppDatabase database, int recipeId) {
        this.database = database;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeDetailsViewModel(database, recipeId);
    }
}
