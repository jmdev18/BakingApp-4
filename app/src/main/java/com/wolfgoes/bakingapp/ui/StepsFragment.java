package com.wolfgoes.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.adapter.StepAdapter;
import com.wolfgoes.bakingapp.model.Recipe;
import com.wolfgoes.bakingapp.model.Step;
import com.wolfgoes.bakingapp.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment {

    private Recipe mRecipe;

    @BindView(R.id.step_list)
    RecyclerView mRecyclerView;

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(Constants.EXTRA_RECIPE, mRecipe);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(Constants.EXTRA_RECIPE);
        } else {
            Step ingredient = new Step();
            ingredient.shortDescription = getString(R.string.ingredients);
            mRecipe.steps.add(0, ingredient);
        }

        StepAdapter stepAdapter = new StepAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(stepAdapter);

        stepAdapter.swapList(mRecipe.steps);

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }
}
