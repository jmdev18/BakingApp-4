package com.wolfgoes.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.adapter.RecipeAdapter;
import com.wolfgoes.bakingapp.model.Recipe;
import com.wolfgoes.bakingapp.network.Controller;
import com.wolfgoes.bakingapp.network.api.RecipeApi;
import com.wolfgoes.bakingapp.util.Constants;
import com.wolfgoes.bakingapp.view.DynamicSpanRecyclerView;

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

    @BindView(R.id.recipe_list)
    DynamicSpanRecyclerView mRecyclerView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.STATE_EXTRA_RECIPES_ARRAY, mRecipes);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(Constants.STATE_EXTRA_RECIPES_ARRAY);
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

        Intent intent = new Intent(this, StepsActivity.class);
        intent.putExtra(Constants.EXTRA_RECIPE, recipe);
        startActivity(intent);
    }
}
