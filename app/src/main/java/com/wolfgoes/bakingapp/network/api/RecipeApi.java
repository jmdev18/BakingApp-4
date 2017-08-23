package com.wolfgoes.bakingapp.network.api;

import com.wolfgoes.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApi {

    @GET("baking.json")
    Call<ArrayList<Recipe>> loadRecipes();

}
