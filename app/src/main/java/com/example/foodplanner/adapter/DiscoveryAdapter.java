package com.example.foodplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Entity.Category;
import com.example.foodplanner.Entity.Country;
import com.example.foodplanner.R;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
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
            return new CaticoryViewHolder(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discovery_country_list, parent, false);
            return new CountryViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChipSelectedType item = itemList.get(position);
        if (holder instanceof CaticoryViewHolder) {
            ((CaticoryViewHolder) holder).bind((Category) item);
        } else if (holder instanceof  CountryViewHolder) {
            ((CountryViewHolder) holder).bind((Country) item);

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }


    static class CaticoryViewHolder extends RecyclerView.ViewHolder {
        public CaticoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        void bind(Category category) {
            ImageView categoryImage=itemView.findViewById(R.id.categoryImage);
            TextView categoryName=itemView.findViewById(R.id.categoryName);
            categoryName.setText(category.getName());
            Glide.with(itemView.getContext()).load(category.getImageUrl()).circleCrop().into(categoryImage);


        }
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        void bind(Country country) {
            ImageView countryImage=itemView.findViewById(R.id.countryImage);
            Glide.with(itemView.getContext()).load(country.getImageUrl()).circleCrop().into(countryImage);


        }
    }
}
