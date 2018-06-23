package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import ru.develop_for_android.bakingapp.database.AppDatabase;

import static ru.develop_for_android.bakingapp.RecipeDetailFragment.RECIPE_TITLE_KEY;

public class RecipeDetailActivity extends AppCompatActivity implements CookingStepClickListener {

    private int recipeId;
    private String recipeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent incomingData = getIntent();
        if (incomingData != null) {
            recipeId = incomingData.getIntExtra(RecipeDetailFragment.RECIPE_ID_KEY, 1);
            recipeTitle = incomingData.getStringExtra(RECIPE_TITLE_KEY);
            Log.i("LIVE", "there is intent with recipeId " + recipeId);
        }
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(RecipeDetailFragment.RECIPE_ID_KEY, 1);
            recipeTitle = savedInstanceState.getString(RECIPE_TITLE_KEY);
            Log.i("LIVE", "there is savedInstanceState with recipeId " + recipeId);
        }
        if (recipeTitle != null) {
            RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(
                    AppDatabase.getInstance(getApplicationContext().getApplicationContext()), recipeId);
            RecipeDetailsViewModel viewModel = ViewModelProviders.of(this, factory)
                    .get(RecipeDetailsViewModel.class);

        }

        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(recipeTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(RecipeDetailFragment.RECIPE_TITLE_KEY, recipeTitle);
        outState.putInt(RecipeDetailFragment.RECIPE_ID_KEY, recipeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(int stepId) {
        Intent openStepDetails = new Intent(getBaseContext(), CookingStepDetailsActivity.class);
        openStepDetails.putExtra(CookingStepDetailsActivity.STEP_ID_KEY, stepId);
        openStepDetails.putExtra(CookingStepDetailsActivity.RECIPE_ID_KEY, recipeId);
        openStepDetails.putExtra(CookingStepDetailsActivity.RECIPE_TITLE_KEY, recipeTitle);
        startActivity(openStepDetails);
    }
}
