package com.wolfgoes.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.adapter.RecipeAdapter;
import com.wolfgoes.bakingapp.model.Recipe;
import com.wolfgoes.bakingapp.network.Controller;
import com.wolfgoes.bakingapp.network.api.RecipeApi;
import com.wolfgoes.bakingapp.util.Constants;
import com.wolfgoes.bakingapp.view.DynamicSpanRecyclerView;
import com.wolfgoes.bakingapp.widget.RecipeWidgetProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class RecipeListActivity extends AppCompatActivity implements Callback<ArrayList<Recipe>>, RecipeAdapter.OnItemClickListener {

    private ArrayList<Recipe> mRecipes;
    private RecipeAdapter mRecipeAdapter;
    private boolean mIsChooseRecipe;

    @BindView(R.id.recipe_list)
    DynamicSpanRecyclerView mRecyclerView;
    private int mWidgetId;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.STATE_EXTRA_RECIPES_ARRAY, mRecipes);
        outState.putBoolean(Constants.STATE_EXTRA_CHOOSE_RECIPE, mIsChooseRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mIsChooseRecipe = getIntent().getBooleanExtra(Constants.EXTRA_CHOOSE_RECIPE, false);
        mWidgetId = getIntent().getIntExtra(Constants.EXTRA_WIDGET_ID, 0);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(Constants.STATE_EXTRA_RECIPES_ARRAY);
            mIsChooseRecipe = savedInstanceState.getBoolean(Constants.STATE_EXTRA_CHOOSE_RECIPE, false);
        }

        mRecipeAdapter = new RecipeAdapter(mRecipes);
        mRecipeAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecyclerView.setHasFixedSize(true);

        if (mRecipes == null) {
            Controller controller = new Controller();
            Retrofit retrofit = controller.getRetrofit();

            RecipeApi recipeApi = retrofit.create(RecipeApi.class);

            Call<ArrayList<Recipe>> call = recipeApi.loadRecipes();
            call.enqueue(this);
        }
    }

    @Override
    public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                mRecipes = response.body();
                mRecipeAdapter.swapList(mRecipes);

                for (Recipe recipe : mRecipes) {
                    Timber.d(recipe.name);
                }
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {

    }

    @Override
    public void onItemClick(int position) {
        Recipe recipe = mRecipeAdapter.getItem(position);

        if (mIsChooseRecipe) {
            mIsChooseRecipe = false;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();
            editor.putString(Integer.toString(mWidgetId), gson.toJson(recipe));
            editor.commit();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
            RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_content_list);

            finish();
        } else {
            Intent intent = new Intent(this, StepsActivity.class);
            intent.putExtra(Constants.EXTRA_RECIPE, recipe);
            startActivity(intent);
        }
    }
}
