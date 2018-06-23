package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.database.CookingStepEntry;

public class StepDetailsViewModel extends ViewModel {

    private LiveData<CookingStepEntry> stepEntry;

    StepDetailsViewModel(AppDatabase database, int stepId) {
        stepEntry = database.recipeDao().loadStepById(stepId);
    }

    public LiveData<CookingStepEntry> getStepEntry() {
        return stepEntry;
    }
}
