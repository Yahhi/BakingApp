package ru.develop_for_android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import ru.develop_for_android.bakingapp.AppExecutors;
import ru.develop_for_android.bakingapp.R;
import ru.develop_for_android.bakingapp.database.AppDatabase;
import ru.develop_for_android.bakingapp.database.IngredientEntry;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsListWidget extends AppWidgetProvider {

    private static List<IngredientEntry> ingredients;
    public static final String KEY_LAST_SELECTED_RECIPE = "last_recipe_id";

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final int recipeId = preferences.getInt(KEY_LAST_SELECTED_RECIPE, 1);
        // Here we setup the intent which points to the StackViewService which will
        // provide the views for this collection.
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.i("WIDGET", "getting database values");
                List<IngredientEntry> ingredients = AppDatabase.getInstance(context).recipeDao().loadFinalIngredientsForRecipe(recipeId);
                String recipeName = AppDatabase.getInstance(context).recipeDao().loadFinalRecipeName(recipeId);
                Log.i("WIDGET", "found " + ingredients.size() + " entries");
                Intent intent = new Intent(context, WidgetService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                ArrayList<String> ingredientTitles = new ArrayList<>();
                ArrayList<String> ingredientMeasures = new ArrayList<>();
                for (IngredientEntry ingredient : ingredients) {
                    ingredientTitles.add(ingredient.getName());
                    ingredientMeasures.add(ingredient.formMeasureString());
                }

                intent.putExtra(WidgetViewsFactory.KEY_INGREDIENT_TITLES, ingredientTitles);
                intent.putExtra(WidgetViewsFactory.KEY_INGREDIENT_MEASURES, ingredientMeasures);
                // When intents are compared, the extras are ignored, so we need to embed the extras
                // into the data so that the extras will not be ignored.
                //intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredients_list_widget);
                rv.setRemoteAdapter(R.id.stack_view, intent);
                rv.setTextViewText(R.id.widget_recipe_title, recipeName);

                // The empty view is displayed when the collection has no items. It should be a sibling
                // of the collection view.
                rv.setEmptyView(R.id.stack_view, R.id.empty_view);

                // Here we setup the a pending intent template. Individuals items of a collection
                // cannot setup their own pending intents, instead, the collection as a whole can
                // setup a pending intent template, and the individual items can set a fillInIntent
                // to create unique before on an item to item basis.
                appWidgetManager.updateAppWidget(appWidgetId, rv);
            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // Enter relevant functionality for when the first ru.develop_for_android.bakingapp.widget is created
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        // Enter relevant functionality for when the last ru.develop_for_android.bakingapp.widget is disabled
    }
}

