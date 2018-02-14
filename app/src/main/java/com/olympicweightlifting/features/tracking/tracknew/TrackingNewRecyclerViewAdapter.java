package com.olympicweightlifting.features.tracking.tracknew;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.tracking.TrackedExerciseData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vangor on 01/02/2018.
 */

public class TrackingNewRecyclerViewAdapter extends RecyclerView.Adapter<TrackingNewRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private List<TrackedExerciseData> trackedExercisesList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.records_layout)
        ConstraintLayout recordsLayout;

        @BindView(R.id.exercise_name)
        TextView exerciseName;
        @BindView(R.id.lifted_weight)
        TextView lifted_weight;
        @BindView(R.id.reps)
        TextView reps;
        @BindView(R.id.sets)
        TextView sets;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public TrackingNewRecyclerViewAdapter(List<TrackedExerciseData> trackedExerciseData, Context context) {
        this.trackedExercisesList = trackedExerciseData;
        this.context = context;
    }

    @Override
    public TrackingNewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_tracking_exercise_card, parent, false);
        return new TrackingNewRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(TrackingNewRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        TrackedExerciseData currentTrackedExercise = trackedExercisesList.get(position);

        viewHolder.recordsLayout.setOnLongClickListener(view -> {
            trackedExercisesList.remove(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());
            return true;
        });

        viewHolder.exerciseName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(21)});
        viewHolder.exerciseName.setText(currentTrackedExercise.getExercise());

        viewHolder.lifted_weight.setText(String.format("%s %s", currentTrackedExercise.getWeightFormatted(), currentTrackedExercise.getUnits().toLowerCase()));
        viewHolder.reps.setText(context.getResources().getQuantityString(R.plurals.repetitons, currentTrackedExercise.getReps(), currentTrackedExercise.getReps()));
        viewHolder.sets.setText(context.getResources().getQuantityString(R.plurals.repetitons, currentTrackedExercise.getSets(), currentTrackedExercise.getSets()));
    }


    @Override
    public int getItemCount() {
        return trackedExercisesList.size();
    }

}
