package com.wolfgoes.bakingapp.model;

import java.util.ArrayList;

public class Recipe {

    public long id;
    public String name;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<Step> steps;
    public int servings;
    public String image;

}
