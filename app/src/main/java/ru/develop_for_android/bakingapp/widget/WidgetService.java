package ru.develop_for_android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import ru.develop_for_android.bakingapp.R;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewsFactory(this.getApplicationContext(), intent);
    }
}

class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public static final String KEY_INGREDIENT_TITLES = "titles";
    public static final String KEY_INGREDIENT_MEASURES = "measures";

    private static final int mCount = 10;
    private ArrayList<String> ingredientTitles, ingredientMeasures;
    private Context mContext;
    private int mAppWidgetId;

    public WidgetViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        ingredientTitles = intent.getStringArrayListExtra(KEY_INGREDIENT_TITLES);
        ingredientMeasures = intent.getStringArrayListExtra(KEY_INGREDIENT_MEASURES);
        Log.i("WIDGET", "starting widgetViewsFactory with " + ingredientTitles.size() + " values");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientTitles.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_ingredient);
        rv.setTextViewText(R.id.ingredient_title, ingredientTitles.get(position));
        rv.setTextViewText(R.id.ingredient_quantity, ingredientMeasures.get(position));
        Log.i("WIDGET", "settings views at " + position + " with " + ingredientTitles.get(position));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
