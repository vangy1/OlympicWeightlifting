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
import com.olympicweightlifting.features.tracking.data.TrackedExerciseData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackingNewRecyclerViewAdapter extends RecyclerView.Adapter<TrackingNewRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private List<TrackedExerciseData> trackedExercisesList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_workouts)
        ConstraintLayout layoutWorkouts;

        @BindView(R.id.text_exercise_name)
        TextView textViewExerciseName;
        @BindView(R.id.text_lifted_weight)
        TextView textViewLiftedWeight;
        @BindView(R.id.text_reps)
        TextView textViewReps;
        @BindView(R.id.text_sets)
        TextView textViewSets;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    TrackingNewRecyclerViewAdapter(List<TrackedExerciseData> trackedExerciseData, Context context) {
        this.trackedExercisesList = trackedExerciseData;
        this.context = context;
    }

    @Override
    public TrackingNewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_tracking_exercise, parent, false);
        return new TrackingNewRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(TrackingNewRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        TrackedExerciseData currentTrackedExercise = trackedExercisesList.get(position);

        viewHolder.layoutWorkouts.setOnLongClickListener(view -> {
            trackedExercisesList.remove(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());
            return true;
        });

        viewHolder.textViewExerciseName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(21)});
        viewHolder.textViewExerciseName.setText(currentTrackedExercise.getExercise());

        viewHolder.textViewLiftedWeight.setText(String.format("%s %s", currentTrackedExercise.getWeightFormatted(), currentTrackedExercise.getUnits().toLowerCase()));
        viewHolder.textViewReps.setText(context.getResources().getQuantityString(R.plurals.repetitons, currentTrackedExercise.getReps(), currentTrackedExercise.getReps()));
        viewHolder.textViewSets.setText(context.getResources().getQuantityString(R.plurals.sets, currentTrackedExercise.getSets(), currentTrackedExercise.getSets()));
    }


    @Override
    public int getItemCount() {
        return trackedExercisesList.size();
    }

}
