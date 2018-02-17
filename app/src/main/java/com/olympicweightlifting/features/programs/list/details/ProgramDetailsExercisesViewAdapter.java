package com.olympicweightlifting.features.programs.list.details;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.ProgramExercise;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProgramDetailsExercisesViewAdapter extends RecyclerView.Adapter<ProgramDetailsExercisesViewAdapter.ViewHolder> {
    private final Fragment parentFragment;
    private List<ProgramExercise> exercises;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.exercise_name)
        TextView exerciseName;
        @BindView(R.id.sets_value)
        TextView reps;
        @BindView(R.id.reps_value)
        TextView sets;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProgramDetailsExercisesViewAdapter(List<ProgramExercise> exercises, Fragment parentFragment) {
        this.exercises = exercises;
        this.parentFragment = parentFragment;
    }

    @Override
    public ProgramDetailsExercisesViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_custom_workout_exercise_card, parent, false);
        return new ProgramDetailsExercisesViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgramDetailsExercisesViewAdapter.ViewHolder viewHolder, int position) {

        ProgramExercise currentProgramExercise = exercises.get(position);
        viewHolder.exerciseName.setText(currentProgramExercise.getExerciseName());
        viewHolder.reps.setText(String.valueOf(currentProgramExercise.getReps()));
        viewHolder.sets.setText(String.valueOf(currentProgramExercise.getSets()));

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }


}
