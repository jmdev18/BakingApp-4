package com.wolfgoes.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.model.Ingredient;
import com.wolfgoes.bakingapp.model.Recipe;
import com.wolfgoes.bakingapp.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import timber.log.Timber;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.step_description)
    TextView mStepDescription;

    @Nullable
    @BindView(R.id.previous_button)
    View mPreviousButton;

    @Nullable
    @BindView(R.id.previous_button_text)
    TextView mPreviousButtonText;

    @Nullable
    @BindView(R.id.next_button)
    View mNextButton;

    @Nullable
    @BindView(R.id.next_button_text)
    TextView mNextButtonText;

    private View mRootView;
    private int mSelected;
    private Recipe mRecipe;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.STATE_EXTRA_RECIPE, mRecipe);
        outState.putInt(Constants.STATE_EXTRA_POSITION, mSelected);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, mRootView);

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ViewTreeObserver.OnGlobalLayoutListener thisObserver = this;

                mPlayerView.post(new Runnable() {
                    public void run() {
                        Timber.d("mRootView.getWidth() %d", mRootView.getWidth());
                        mPlayerView.getLayoutParams().height = (int) ((float) mRootView.getWidth() * (9.0 / 16.0));
                        mPlayerView.requestLayout();
                        mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(thisObserver);
                    }
                });
            }
        });

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(Constants.STATE_EXTRA_RECIPE);
            mSelected = savedInstanceState.getInt(Constants.STATE_EXTRA_POSITION);
        }

        initView();

        return mRootView;
    }

    @Optional
    @OnClick(R.id.previous_button)
    public void goPrevious() {
        mSelected--;
        initView();
    }

    @Optional
    @OnClick(R.id.next_button)
    public void goNext() {
        mSelected++;
        initView();
    }

    private void initView() {
        if (mSelected == 0) {
            showIngredients();
        } else {
            showSteps();
        }

        if (!isTablet()) {
            if (mSelected == 0) {
                mPreviousButtonText.setEnabled(false);
                mPreviousButton.setClickable(false);
            } else {
                mPreviousButtonText.setEnabled(true);
                mPreviousButton.setClickable(true);
            }

            if (mSelected == mRecipe.steps.size() - 1) {
                mNextButtonText.setEnabled(false);
                mNextButton.setClickable(false);
            } else {
                mNextButtonText.setEnabled(true);
                mNextButton.setClickable(true);
            }
        }
    }

    private void showSteps() {
        if (TextUtils.isEmpty(mRecipe.steps.get(mSelected).videoURL)) {
            mPlayerView.setVisibility(View.GONE);
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
        }
        mStepDescription.setText(mRecipe.steps.get(mSelected).description);
    }

    private void showIngredients() {
        mPlayerView.setVisibility(View.GONE);
        String step = "";
        for (Ingredient ingredient : mRecipe.ingredients) {
            step += ingredient.getString(getContext()) + "\n";
        }
        mStepDescription.setText(step);
    }

    public void setSelected(int selected) {
        mSelected = selected;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    private boolean isTablet() {
        return mPreviousButton == null;
    }
}
