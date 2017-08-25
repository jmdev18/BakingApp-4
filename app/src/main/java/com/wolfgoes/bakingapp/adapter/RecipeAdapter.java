package com.wolfgoes.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolfgoes.bakingapp.GlideApp;
import com.wolfgoes.bakingapp.R;
import com.wolfgoes.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private ArrayList<Recipe> mRecipes;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_image)
        ImageView mRecipeImage;

        @BindView(R.id.recipe_name)
        TextView mRecipeName;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mRecipeImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick(this);
        }
    }

    private void itemClick(RecipeViewHolder holder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(holder.getAdapterPosition());
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false);

        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (layoutParams.height == RecyclerView.LayoutParams.WRAP_CONTENT) {
            int defaultHeight = mContext.getResources().getDimensionPixelSize(R.dimen.recipe_item_height);
            int defaultWidth = mContext.getResources().getDimensionPixelSize(R.dimen.recipe_item_width);

            int parentWidth = parent.getWidth();
            int parentMargin = mContext.getResources().getDimensionPixelSize(R.dimen.activity_margin);
            parentWidth -= (2 * parentMargin);

            float proportion = (float) defaultHeight / (float) defaultWidth;
            layoutParams.height = (int) (parentWidth * proportion);
            itemView.setLayoutParams(layoutParams);
        }

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        holder.mRecipeName.setText(recipe.name);

        GlideApp.with(mContext)
                .load(recipe.image)
                .placeholder(R.drawable.default_recipe)
                .into(holder.mRecipeImage);
    }

    @Override
    public int getItemCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

    public void swapList(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public Recipe getItem(int position) {
        return mRecipes.get(position);
    }
}
