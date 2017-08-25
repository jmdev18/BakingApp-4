package com.wolfgoes.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private ArrayList<Step> mSteps;
    private int mSelectedPosition;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_short_description)
        TextView mStepShortDescription;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick(this);
        }
    }

    private void itemClick(StepViewHolder holder) {
        mSelectedPosition = holder.getAdapterPosition();
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(holder.getAdapterPosition());
        }
        notifyDataSetChanged();
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = mSteps.get(position);

        holder.mStepShortDescription.setText(step.shortDescription);
        holder.itemView.setSelected(position == mSelectedPosition);
    }

    @Override
    public int getItemCount() {
        return mSteps == null ? 0 : mSteps.size();
    }

    public void swapList(ArrayList<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public Step getItem(int position) {
        return mSteps.get(position);
    }
}
