package com.example.foodplanner.Presentation.discoveryScreen.view;

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

import java.util.ArrayList;
import java.util.List;

public class SearchMealAdapter extends RecyclerView.Adapter<SearchMealAdapter.ViewHolder> {
    private List<Meal> meals = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Meal meal);
    }

    public SearchMealAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setList(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView categoryTextView;
        TextView areaTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_item_image);
            nameTextView = itemView.findViewById(R.id.search_item_name);
            categoryTextView = itemView.findViewById(R.id.search_item_category);
            areaTextView = itemView.findViewById(R.id.search_item_area);
        }

        void bind(final Meal meal) {
            nameTextView.setText(meal.getName());
            categoryTextView.setText(meal.getCategory());
            areaTextView.setText(meal.getArea());

            Glide.with(context)
                    .load(meal.getThumbnailUrl())
                    .placeholder(R.drawable.ic_search)
                    .error(R.drawable.ic_search)
                    .into(imageView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(meal);
                }
            });
        }
    }
}
