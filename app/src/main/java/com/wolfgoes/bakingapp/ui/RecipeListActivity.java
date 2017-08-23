package com.wolfgoes.bakingapp.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.model.Recipe;
import com.wolfgoes.bakingapp.network.Controller;
import com.wolfgoes.bakingapp.network.api.RecipeApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecipeListActivity extends AppCompatActivity implements Callback<ArrayList<Recipe>> {

    private static final String LOG_TAG = RecipeListActivity.class.getSimpleName();

    private ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Controller controller = new Controller();
        Retrofit retrofit = controller.getRetrofit();

        RecipeApi recipeApi = retrofit.create(RecipeApi.class);

        Call<ArrayList<Recipe>> call = recipeApi.loadRecipes();
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                mRecipes = response.body();

                for (Recipe recipe : mRecipes) {
                    Log.d(LOG_TAG, recipe.name);
                }
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {

    }
}
