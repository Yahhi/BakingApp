package ru.develop_for_android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.JobIntentService;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.networking.RecipesLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int count = AppDatabase.getInstance(getApplicationContext()).recipeDao().getRecipesCount();
                if (count == 0) {
                    JobIntentService.enqueueWork(getBaseContext(), RecipesLoader.class, 1234, new Intent());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
