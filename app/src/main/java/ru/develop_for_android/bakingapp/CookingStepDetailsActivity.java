package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.database.CookingStepEntry;

public class CookingStepDetailsActivity extends AppCompatActivity {

    public static final String RECIPE_ID_KEY = "recipe_id";
    public static final String STEP_ID_KEY = "step_id";
    public static final String RECIPE_TITLE_KEY = "recipe_title";

    int recipeId, stepId;
    String recipeTitle;
    int[] stepIds;
    List<CookingStepEntry> steps;
    MenuItem back, forward, active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent incomingData = getIntent();
        if (incomingData != null) {
            recipeTitle = incomingData.getStringExtra(RECIPE_TITLE_KEY);

            recipeId = incomingData.getIntExtra(RECIPE_ID_KEY, 1);
            stepId = incomingData.getIntExtra(STEP_ID_KEY, 1);

        }
        if (savedInstanceState != null) {
            recipeTitle = savedInstanceState.getString(RECIPE_TITLE_KEY);

            recipeId = savedInstanceState.getInt(RECIPE_ID_KEY, 1);
            stepId = savedInstanceState.getInt(STEP_ID_KEY, 1);

        }
        RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(
                AppDatabase.getInstance(this), recipeId);
        final RecipeDetailsViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(RecipeDetailsViewModel.class);
        viewModel.getSteps().observe(this, new Observer<List<CookingStepEntry>>() {
            @Override
            public void onChanged(@Nullable List<CookingStepEntry> cookingStepEntries) {
                steps = cookingStepEntries;
                stepIds = new int[cookingStepEntries.size()];
                int i = 0;
                for (CookingStepEntry step : cookingStepEntries) {
                    stepIds[i++] = step.getId();
                    if (stepId == step.getId()) {
                        viewModel.selectStep(step);
                    }
                }
            }
        });
        viewModel.getStepEntry().observe(this, new Observer<CookingStepEntry>() {
            @Override
            public void onChanged(@Nullable CookingStepEntry cookingStepEntry) {
                if (cookingStepEntry == null) {
                    return;
                }
                stepId = cookingStepEntry.getId();
                active.setTitle(String.valueOf(cookingStepEntry.getOrderingId()));
                if (stepIds == null || stepIds[0] == stepId) {
                    back.setEnabled(false);
                } else {
                    back.setEnabled(true);
                }
                if (stepIds == null || stepIds[stepIds.length - 1] == stepId) {
                    forward.setEnabled(false);
                } else {
                    forward.setEnabled(true);
                }
            }
        });

        setTitle(recipeTitle);
        setContentView(R.layout.activity_cooking_step_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.steps_navigation);
        Menu bottomMenu = bottomNavigationView.getMenu();
        active = bottomMenu.findItem(R.id.step_active);
        back = bottomMenu.findItem(R.id.step_back);
        forward = bottomMenu.findItem(R.id.step_forward);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.step_back:
                        for (int i = 1; i < stepIds.length; i++) {
                            if (stepIds[i] == stepId) {
                                viewModel.selectStep(steps.get(i-1));
                                break;
                            }
                        }
                        return true;
                    case R.id.step_forward:
                        for (int i = 0; i < stepIds.length - 1; i++) {
                            if (stepIds[i] == stepId) {
                                viewModel.selectStep(steps.get(i+1));
                                break;
                            }
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(RECIPE_TITLE_KEY, recipeTitle);
        outState.putInt(RECIPE_ID_KEY, recipeId);
        outState.putInt(STEP_ID_KEY, stepId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
