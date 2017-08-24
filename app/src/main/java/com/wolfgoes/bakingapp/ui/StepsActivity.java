package com.wolfgoes.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.model.Recipe;
import com.wolfgoes.bakingapp.util.Constants;

public class StepsActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.STATE_EXTRA_RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mRecipe = getIntent().getParcelableExtra(Constants.EXTRA_RECIPE);

        if (mRecipe == null && savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(Constants.STATE_EXTRA_RECIPE);
        }

        if (mRecipe != null) {
            setTitle(mRecipe.name);

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (savedInstanceState == null) {

                StepsFragment stepsFragment = new StepsFragment();
                stepsFragment.setRecipe(mRecipe);

                fragmentManager.beginTransaction()
                        .add(R.id.steps_list_container, stepsFragment)
                        .commit();
            }

            if (findViewById(R.id.step_detail_container) != null) {
                mTwoPane = true;

                if (savedInstanceState == null) {
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();
                    fragmentManager.beginTransaction()
                            .add(R.id.step_detail_container, stepDetailFragment)
                            .commit();
                }
            } else {
                mTwoPane = false;
            }
        }
    }
}
