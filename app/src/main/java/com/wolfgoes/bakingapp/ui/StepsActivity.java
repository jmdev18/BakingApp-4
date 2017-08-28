package com.wolfgoes.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.model.Recipe;
import com.wolfgoes.bakingapp.util.Constants;

public class StepsActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private Recipe mRecipe;
    private int mSelected = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.STATE_EXTRA_RECIPE, mRecipe);
        outState.putInt(Constants.STATE_EXTRA_POSITION, mSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_steps);

        mRecipe = getIntent().getParcelableExtra(Constants.EXTRA_RECIPE);

        if (mRecipe == null && savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(Constants.STATE_EXTRA_RECIPE);
            mSelected = savedInstanceState.getInt(Constants.STATE_EXTRA_POSITION);
        }

        setTitle(mRecipe.name);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setSelected(mSelected);
            stepsFragment.setRecipe(mRecipe);

            fragmentManager.beginTransaction()
                    .add(R.id.steps_list_container, stepsFragment)
                    .commit();
        }

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setSelected(mSelected);
                stepDetailFragment.setRecipe(mRecipe);
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(int position) {
        mSelected = position;

        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setSelected(mSelected);
            stepDetailFragment.setRecipe(mRecipe);
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(Constants.EXTRA_RECIPE, mRecipe);
            intent.putExtra(Constants.EXTRA_POSITION, position);
            startActivity(intent);
        }
    }
}
