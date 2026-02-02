package com.example.foodplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Entity.Meal;
import com.example.foodplanner.R;
import com.example.foodplanner.service.CountryCodeService;

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

        String code = CountryCodeService.getCountryCode(meal.getArea());

        if (code != null) {
            String flagUrl = "https://flagcdn.com/w160/" + code.toLowerCase() + ".png";
            Glide.with(context).load(flagUrl).circleCrop().into(holder.flagImage);
        }
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
