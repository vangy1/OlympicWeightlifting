package com.olympicweightlifting.features.programs.list.details;

import android.content.Context;
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
    private final Context context;
    private List<ProgramExercise> exercises;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_exercise_name)
        TextView exerciseName;
        @BindView(R.id.text_reps_value)
        TextView textViewRepsValue;
        @BindView(R.id.text_sets_value)
        TextView textViewSetsValue;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProgramDetailsExercisesViewAdapter(List<ProgramExercise> exercises, Context context) {
        this.exercises = exercises;
        this.context = context;
    }

    @Override
    public ProgramDetailsExercisesViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_programs_exercise, parent, false);
        return new ProgramDetailsExercisesViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgramDetailsExercisesViewAdapter.ViewHolder viewHolder, int position) {

        ProgramExercise currentProgramExercise = exercises.get(position);
        viewHolder.exerciseName.setText(currentProgramExercise.getExerciseName());
        viewHolder.textViewSetsValue.setText(String.valueOf(currentProgramExercise.getReps()));
        viewHolder.textViewRepsValue.setText(String.valueOf(currentProgramExercise.getSets()));

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }


}
