package com.olympicweightlifting.features.tracking.history;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.tracking.TrackedWorkoutData;
import com.olympicweightlifting.features.tracking.history.details.TrackingWorkoutDetails;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateFormat.getDateFormat;

public class TrackingHistoryRecyclerViewAdapter extends RecyclerView.Adapter<TrackingHistoryRecyclerViewAdapter.ViewHolder> {
    private Activity activity;
    private DateFormat dateFormat;

    private List<TrackedWorkoutData> trackedWorkoutsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_records)
        ConstraintLayout recordsLayout;

        @BindView(R.id.text_workout_date)
        TextView workoutDate;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public TrackingHistoryRecyclerViewAdapter(List<TrackedWorkoutData> trackedWorkoutsList, Activity actvitiy) {
        this.trackedWorkoutsList = trackedWorkoutsList;
        this.activity = actvitiy;
        dateFormat = getDateFormat(actvitiy.getApplicationContext());
    }

    @Override
    public TrackingHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_tracking_history, parent, false);
        return new TrackingHistoryRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(TrackingHistoryRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        TrackedWorkoutData currentTrackedWorkout = trackedWorkoutsList.get(position);

        viewHolder.workoutDate.setText(dateFormat.format(currentTrackedWorkout.getDateOfWorkout()));
        viewHolder.recordsLayout.setOnClickListener(view -> {
            showWorkoutDetails(currentTrackedWorkout);
        });
    }

    private void showWorkoutDetails(TrackedWorkoutData currentTrackedWorkout) {
        Bundle bundle = new Bundle();
        bundle.putString("workoutDetails", new Gson().toJson(currentTrackedWorkout));
        TrackingWorkoutDetails trackingWorkoutDetails = TrackingWorkoutDetails.newInstance(bundle);
        activity.getFragmentManager().beginTransaction()
                .add(R.id.fragment_container_workouts, trackingWorkoutDetails, "workoutDetailsFragment")
                .addToBackStack("workoutDetailsFragment")
                .commit();

        View workoutDetailsFragmentContainer = activity.findViewById(R.id.fragment_container_workouts);
        workoutDetailsFragmentContainer.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return trackedWorkoutsList.size();
    }

}
