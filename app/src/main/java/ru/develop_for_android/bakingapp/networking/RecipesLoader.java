package ru.develop_for_android.bakingapp.networking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.develop_for_android.bakingapp.AppExecutors;
import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.database.CookingStepEntry;
import ru.develop_for_android.bakingapp.database.IngredientEntry;
import ru.develop_for_android.bakingapp.database.RecipeEntry;

public class RecipesLoader extends JobIntentService {

    public static final String KEY_DOWNLOAD_COMPLETE = "downloaded";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("NETWORK", "request to get network");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        final JsonArrayRequest request = new JsonArrayRequest(JsonObjectRequest.Method.GET, url,
                null, new  Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final AppDatabase database = AppDatabase.getInstance(getBaseContext());
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.recipeDao().clearRecipes();
                    }
                });

                Log.i("NETWORK", response.toString());
                final RecipeEntry[] recipeEntries = new RecipeEntry[response.length()];
                final ArrayList<IngredientEntry> ingredientEntries = new ArrayList<>();
                final ArrayList<CookingStepEntry> cookingStepEntries = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject recipeObject = response.getJSONObject(i);
                        int id = recipeObject.getInt("id");
                        String name = recipeObject.getString("name");
                        int servings = recipeObject.getInt("servings");
                        String image = recipeObject.getString("image");
                        recipeEntries[i] = new RecipeEntry(id, name, servings, image);

                        JSONArray ingredientsArray = recipeObject.getJSONArray("ingredients");
                        for (int j = 0; j < ingredientsArray.length(); j++) {
                            JSONObject ingredientObject = ingredientsArray.getJSONObject(j);
                            ingredientEntries.add(new IngredientEntry(recipeEntries[i].getId(),
                                    ingredientObject.getString("ingredient"),
                                    ingredientObject.getString("measure"),
                                    ingredientObject.getDouble("quantity")));

                        }

                        JSONArray stepsArray = recipeObject.getJSONArray("steps");
                        for (int j = 0; j < stepsArray.length(); j++) {
                            JSONObject stepObject = stepsArray.getJSONObject(j);
                            cookingStepEntries.add(new CookingStepEntry(stepObject.getInt("id"),
                                    stepObject.getString("shortDescription"),
                                    stepObject.getString("description"),
                                    stepObject.getString("videoURL"),
                                    stepObject.getString("thumbnailURL"),
                                    recipeEntries[i].getId()));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.recipeDao().insertJsonResults(
                                recipeEntries, ingredientEntries, cookingStepEntries);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("NETWORK", "Network problem. " + error.getMessage());
            }
        });
        queue.add(request);
    }
}
