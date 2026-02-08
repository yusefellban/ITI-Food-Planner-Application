package com.example.foodplanner.Presentation.homeScreen.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Data.meals.model.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.Data.meals.datasource.local.CountryCodeLocalDataSource;
import com.example.foodplanner.Data.meals.model.wrapper.SelectedMeal;

import java.util.List;

public class HomeCarouselAdapter extends RecyclerView.Adapter<HomeCarouselAdapter.ViewHolder> {

    private List<Meal> mealList;
    private Context context;
    private CountryCodeLocalDataSource countryCodeLocalDataSource;
    private onItemClickListener onItemClick;

    public HomeCarouselAdapter(Context context, onItemClickListener onItemClick) {
        this.context = context;
        countryCodeLocalDataSource = new CountryCodeLocalDataSource();
        this.onItemClick = onItemClick;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.colum_home_product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = mealList.get(position);

        holder.name.setText(meal.getName());

        Glide.with(context).load(meal.getThumbnailUrl()).into(holder.bgImage);

        String code = countryCodeLocalDataSource.getCountryCode(meal.getArea());

        if (code != null) {
            String flagUrl = "https://flagcdn.com/w160/" + code.toLowerCase() + ".png";
            Glide.with(context).load(flagUrl).circleCrop().into(holder.flagImage);
        }

        //navigate
        holder.itemView.setOnClickListener(v -> {

            SelectedMeal selectedMeal = new SelectedMeal(
                    Integer.parseInt(meal.getId()),
                    meal.getName(),
                    meal.getThumbnailUrl()
            );

            onItemClick.showSelectedMeal(selectedMeal);

        });


    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bgImage, flagImage;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bgImage = itemView.findViewById(R.id.homeProductImage);
            flagImage = itemView.findViewById(R.id.homeProductIcon);
            name = itemView.findViewById(R.id.homeProductMealName);

        }
    }
}
