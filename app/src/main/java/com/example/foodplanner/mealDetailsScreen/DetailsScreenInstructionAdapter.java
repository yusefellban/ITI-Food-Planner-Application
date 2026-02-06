package com.example.foodplanner.mealDetailsScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;

import java.util.List;

public class DetailsScreenInstructionAdapter extends RecyclerView.Adapter<DetailsScreenInstructionAdapter.ViewHolder>{

    private Context context;

    private List<String> instructions;

    public DetailsScreenInstructionAdapter(Context context, List<String> instructions) {
        this.context = context;
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detalis_instructions_rows, parent, false);
        return new DetailsScreenInstructionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      String instruction=instructions.get(position);
      String instructionTitle=getInstructionTitle(instruction);

      holder.stepNumber.setText(String.format("%02d", position + 1));
      holder.title.setText(instructionTitle);
      holder.stepInstruction.setText(instruction);

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
                .setStartDelay(position * 50L) // حركة تتابعية (Cascade)
                .start();

    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView stepNumber,title,stepInstruction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.instructionNumber);
            title = itemView.findViewById(R.id.instructionTitle);
            stepInstruction = itemView.findViewById(R.id.instructionStep);
        }
    }

    public String getInstructionTitle(String instruction) {
        if (instruction == null || instruction.trim().isEmpty()) {
            return "";
        }
        String[] words = instruction.trim().split("\\s+");
        if (words.length >= 2) {
            return words[0] + " " + words[1];
        } else {
            return words[0];
        }
    }
}
