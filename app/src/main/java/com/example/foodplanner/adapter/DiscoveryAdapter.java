package com.example.foodplanner.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.foodplanner.DiscoveryFragment;
import com.example.foodplanner.DiscoveryFragmentDirections;
import com.example.foodplanner.Entity.Category;
import com.example.foodplanner.Entity.Country;
import com.example.foodplanner.Entity.Ingredient;
import com.example.foodplanner.HomeFragmentDirections;
import com.example.foodplanner.R;
import com.example.foodplanner.wrapper.SelectedMeal;
import com.example.foodplanner.wrapper.SendSelectedItem;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChipSelectedType> itemList = new ArrayList<>();

    public void setList(List<ChipSelectedType> newList) {
        this.itemList.clear();
        this.itemList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) { // Country
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dicovery_catigories_list, parent, false);
            return new CatigoryViewHolder(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discovery_country_list, parent, false);
            return new CountryViewHolder(view);
        } else if (viewType == 3) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discovery_ingredient_list, parent, false);
            return new IngredientViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChipSelectedType item = itemList.get(position);
        if (holder instanceof CatigoryViewHolder) {
            ((CatigoryViewHolder) holder).bind((Category) item);
            holder.itemView.setOnClickListener((v) -> {

                SendSelectedItem selectedMeal = new SendSelectedItem(
                        Integer.parseInt(((Category) item).getId()),
                        ((Category) item).getName(),
                        ((Category) item).getImageUrl(), 1
                );
                Log.d("hahah", "ttt" + selectedMeal.toString());

                DiscoveryFragmentDirections.ActionDiscoveryFragmentToFilteredMealsFragment action =
                        DiscoveryFragmentDirections.actionDiscoveryFragmentToFilteredMealsFragment(selectedMeal);

                Navigation.findNavController(v).navigate(action);
            });

        } else if (holder instanceof CountryViewHolder) {
            ((CountryViewHolder) holder).bind((Country) item);
            holder.itemView.setOnClickListener((v) -> {

                SendSelectedItem selectedMeal = new SendSelectedItem(
                        88,
                        ((Country) item).getName(),
                        ((Country) item).getImageUrl(), 3
                );
                Log.d("hahah", "ttt" + selectedMeal.toString());

                DiscoveryFragmentDirections.ActionDiscoveryFragmentToFilteredMealsFragment action =
                        DiscoveryFragmentDirections.actionDiscoveryFragmentToFilteredMealsFragment(selectedMeal);

                Navigation.findNavController(v).navigate(action);
            });

        } else if (holder instanceof IngredientViewHolder) {
            ((IngredientViewHolder) holder).bind((Ingredient) item);
            holder.itemView.setOnClickListener((v) -> {

                SendSelectedItem selectedMeal = new SendSelectedItem(
                        Integer.parseInt(((Ingredient) item).getId()),
                        ((Ingredient) item).getName(),
                        ((Ingredient) item).getImageUrl(), 2
                );
                Log.d("hahah", "ttt" + selectedMeal.toString());

                DiscoveryFragmentDirections.ActionDiscoveryFragmentToFilteredMealsFragment action =
                        DiscoveryFragmentDirections.actionDiscoveryFragmentToFilteredMealsFragment(selectedMeal);

                Navigation.findNavController(v).navigate(action);
            });

        }

        /// Animations

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }


    static class CatigoryViewHolder extends RecyclerView.ViewHolder {
        public CatigoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Category category) {
            ImageView categoryImage = itemView.findViewById(R.id.categoryImage);
            TextView categoryName = itemView.findViewById(R.id.categoryName);
            categoryName.setText(category.getName());
            Glide.with(itemView.getContext())
                    .load(category.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop().into(categoryImage);


        }
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Country country) {
            ImageView countryImage = itemView.findViewById(R.id.countryImage);
            Glide.with(itemView.getContext()).load(country.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop().into(countryImage);


        }
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        void bind(Ingredient ingredient) {
            ImageView ingredientImage = itemView.findViewById(R.id.ingredientImage);
            Glide.with(itemView.getContext()).load(ingredient.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop().into(ingredientImage);
            TextView ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientName.setText(ingredient.getName());
        }
    }
}
