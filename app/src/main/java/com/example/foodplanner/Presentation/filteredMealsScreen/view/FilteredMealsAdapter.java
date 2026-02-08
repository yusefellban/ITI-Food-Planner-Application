package com.example.foodplanner.Presentation.filteredMealsScreen.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;

import java.util.List;

public class FilteredMealsAdapter extends RecyclerView.Adapter<FilteredMealsAdapter.ViewHolder> {

    private List<Meal> mealList;
    private Context context;
    private onFilteredItemClickListener onFilteredItemClickListener;

    public FilteredMealsAdapter(Context context,onFilteredItemClickListener onFilteredItemClickListener) {
        this.context = context;
        this.onFilteredItemClickListener=onFilteredItemClickListener;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public FilteredMealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filtered_meals_list, parent, false);
        return new FilteredMealsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredMealsAdapter.ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.name.setText(meal.getName());
        Glide.with(context).load(meal.getThumbnailUrl()).into(holder.bgImage);

        //navigate
        holder.itemView.setOnClickListener(v -> {

            SelectedMeal selectedMeal = new SelectedMeal(
                    Integer.parseInt(meal.getId()),
                    meal.getName(),
                    meal.getThumbnailUrl()
            );
            onFilteredItemClickListener.showSelectedMeal(selectedMeal);

        });
        /// Animation
        holder.itemView.setAlpha(0f);
        holder.itemView.setScaleX(0.9f);
        holder.itemView.setScaleY(0.9f);
        holder.itemView.setTranslationY(50f);

        holder.itemView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay(position * 50L)
                .start();

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bgImage;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bgImage = itemView.findViewById(R.id.filteredListImage);
            name = itemView.findViewById(R.id.filteredListName);
        }
    }
}
