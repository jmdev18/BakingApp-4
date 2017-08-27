package com.wolfgoes.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.model.Ingredient;
import com.wolfgoes.bakingapp.model.Recipe;
import com.wolfgoes.bakingapp.ui.RecipeListActivity;
import com.wolfgoes.bakingapp.ui.StepsActivity;
import com.wolfgoes.bakingapp.util.Constants;

import timber.log.Timber;

public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, SharedPreferences preferences) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        Intent clickIntent;
        Intent widgetServiceIntent = new Intent(context, ListWidgetService.class);
        widgetServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // when intents are compared, the extras are ignored, so we need to
        // embed the extras into the data so that the extras will not be ignored
        widgetServiceIntent.setData(Uri.parse(widgetServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        if (!preferences.contains(Integer.toString(appWidgetId))) {
            clickIntent = new Intent(context, RecipeListActivity.class);
            clickIntent.putExtra(Constants.EXTRA_CHOOSE_RECIPE, true);
            clickIntent.putExtra(Constants.EXTRA_WIDGET_ID, appWidgetId);

            // Set title
            views.setTextViewText(R.id.widget_title, context.getString(R.string.app_name));
        } else {
            clickIntent = new Intent(context, StepsActivity.class);

            String mRecipeJson = preferences.getString(Integer.toString(appWidgetId), null);

            if (!TextUtils.isEmpty(mRecipeJson)) {
                Gson gson = new Gson();
                Recipe recipe = gson.fromJson(mRecipeJson, Recipe.class);

                // Set title
                views.setTextViewText(R.id.widget_title, recipe.name);
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.root_view, pendingIntent);
        views.setRemoteAdapter(R.id.widget_content_list, widgetServiceIntent);
        views.setPendingIntentTemplate(R.id.widget_content_list, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, preferences);
        }
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, preferences);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int appWidgetId : appWidgetIds) {
            editor.remove(Integer.toString(appWidgetId));
        }
        editor.apply();
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

    public static class ListWidgetService extends RemoteViewsService {

        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
        }

        private class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
            Context mContext;
            int mWidgetId;

            public ListRemoteViewsFactory(Context applicationContext, Intent intent) {
                mContext = applicationContext;
                mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
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
                return 1;
            }

            @Override
            public RemoteViews getViewAt(int i) {
                String content = "";

                Intent fillInIntent = new Intent();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                String mRecipeJson = sharedPreferences.getString(Integer.toString(mWidgetId), null);

                if (TextUtils.isEmpty(mRecipeJson)) {
                    content = mContext.getString(R.string.add_recipe_widget);
                } else {
                    Gson gson = new Gson();
                    Recipe recipe = gson.fromJson(mRecipeJson, Recipe.class);

                    for (Ingredient ingredient : recipe.ingredients) {
                        content += ingredient.getString(mContext) + "\n";
                    }

                    fillInIntent.putExtra(Constants.EXTRA_RECIPE, recipe);
                }

                RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_item);
                views.setTextViewText(R.id.recipe_content, content);
                views.setOnClickFillInIntent(R.id.root_view, fillInIntent);

                return views;
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
            public long getItemId(int i) {
                return 1;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        }
    }
}
