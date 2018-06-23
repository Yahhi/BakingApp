package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ru.develop_for_android.bakingapp.database.AppDatabase;

public class StepDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    AppDatabase database;
    private int stepId;

    StepDetailsViewModelFactory(AppDatabase database, int stepId) {
        this.database = database;
        this.stepId = stepId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StepDetailsViewModel(database, stepId);
    }
}
