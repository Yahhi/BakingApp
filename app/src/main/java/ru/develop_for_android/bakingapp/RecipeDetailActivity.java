package ru.develop_for_android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE_TITLE_KEY = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent incomingData = getIntent();
        if (incomingData != null) {
            int recipeId = incomingData.getIntExtra(RecipeDetailFragment.RECIPE_ID_KEY, 1);
            String recipeTitle = incomingData.getStringExtra(RECIPE_TITLE_KEY);
            setTitle(recipeTitle);
            RecipeDetailFragment detailFragment = RecipeDetailFragment.newInstance(recipeId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_place, detailFragment)
                    .commit();
        }
    }

}
