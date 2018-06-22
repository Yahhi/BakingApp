package ru.develop_for_android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.develop_for_android.bakingapp.database.CookingStepEntry;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private Context context;
    private List<CookingStepEntry> steps;

    StepAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CookingStepEntry stepEntry = steps.get(position);
        String imageAddress = stepEntry.getThumbnailUrl();
        String videoAddress = stepEntry.getVideoUrl();

        holder.stepNumber.setText(String.valueOf(stepEntry.getOrderingId()));
        holder.stepTitle.setText(stepEntry.getShortDescription());
        if (imageAddress == null || imageAddress.equals("")) {
            holder.stepImage.setVisibility(View.GONE);
        } else {
            holder.stepImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(imageAddress).into(holder.stepImage);
        }
        if (videoAddress == null || videoAddress.equals("")) {
            holder.stepVideoExist.setVisibility(View.GONE);
        } else {
            holder.stepVideoExist.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        } else {
            return steps.size();
        }
    }

    public void setSteps(List<CookingStepEntry> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final ImageView stepImage;
        private final ImageView stepVideoExist;
        private final TextView stepTitle;
        private final TextView stepNumber;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            stepImage = view.findViewById(R.id.step_image);
            stepNumber = view.findViewById(R.id.step_number);
            stepTitle = view.findViewById(R.id.step_short_description);
            stepVideoExist = view.findViewById(R.id.step_video_exist);
        }
    }
}
