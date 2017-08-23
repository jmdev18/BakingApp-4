package com.wolfgoes.bakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wolfgoes.bakingapp.R;

public class StepListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        if (findViewById(R.id.step_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }
}
