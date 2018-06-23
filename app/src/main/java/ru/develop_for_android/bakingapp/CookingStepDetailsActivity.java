package ru.develop_for_android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class CookingStepDetailsActivity extends AppCompatActivity {

    public static final String RECIPE_ID_KEY = "recipe_id";
    public static final String STEP_ID_KEY = "step_id";
    public static final String RECIPE_TITLE_KEY = "recipe_title";

    int recipeId, stepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_step_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent incomingData = getIntent();
        if (incomingData != null) {
            String recipeTitle = incomingData.getStringExtra(RECIPE_TITLE_KEY);
            setTitle(recipeTitle);

            recipeId = incomingData.getIntExtra(RECIPE_ID_KEY, 1);
            stepId = incomingData.getIntExtra(STEP_ID_KEY, 1);

            CookingStepDetailsFragment fragment = CookingStepDetailsFragment.newInstance(stepId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_step_details, fragment)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
