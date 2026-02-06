package com.example.foodplanner.homeScreen;

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
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.datasource.local.CountryCodeLocalDataSource;
import com.example.foodplanner.model.wrapper.SelectedMeal;

import java.util.List;

public class HomeCarouselAdapter extends RecyclerView.Adapter<HomeCarouselAdapter.ViewHolder> {

    private List<Meal> mealList;
    private Context context;

    public HomeCarouselAdapter(Context context, List<Meal> mealList) {
        this.context = context;
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

        String code = CountryCodeLocalDataSource.getCountryCode(meal.getArea());

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

            HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                    HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(selectedMeal);

            Navigation.findNavController(v).navigate(action);
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
