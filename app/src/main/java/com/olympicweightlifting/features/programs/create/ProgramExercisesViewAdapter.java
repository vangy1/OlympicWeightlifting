package com.olympicweightlifting.features.programs.create;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.ProgramExercise;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vangor on 01/02/2018.
 */

public class ProgramExercisesViewAdapter extends RecyclerView.Adapter<ProgramExercisesViewAdapter.ViewHolder> {
    private final Context context;
    private List<ProgramExercise> exercises;
    private List<String> userExercises = new ArrayList<>();
    private ArrayAdapter spinnerAdapter;

    static abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        abstract void bind(List<ProgramExercise> exercises, List<String> exerciseList, ArrayAdapter spinnerAdapter, ProgramExercisesViewAdapter programExercisesViewAdapter, Context context);
    }

    static class ShowItemViewHolder extends ProgramExercisesViewAdapter.ViewHolder {
        @BindView(R.id.item_layout)
        ViewGroup itemLayout;

        @BindView(R.id.exercise_name)
        TextView exerciseName;
        @BindView(R.id.reps_value)
        TextView reps;
        @BindView(R.id.sets_value)
        TextView sets;

        ShowItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramExercise> exercises, List<String> exerciseList, ArrayAdapter spinnerAdapter, ProgramExercisesViewAdapter programExercisesViewAdapter, Context context) {
            ProgramExercise currentProgramExercise = exercises.get(getAdapterPosition());
            exerciseName.setText(currentProgramExercise.getExerciseName());
            reps.setText(String.valueOf(currentProgramExercise.getReps()));
            sets.setText(String.valueOf(currentProgramExercise.getSets()));

            itemLayout.setOnLongClickListener(view -> {
                exercises.remove(getAdapterPosition());
                programExercisesViewAdapter.notifyItemRemoved(getAdapterPosition());
                return true;
            });

        }
    }

    static class AddItemViewHolder extends ProgramExercisesViewAdapter.ViewHolder {
        @BindView(R.id.exercise_spinner)
        Spinner exerciseSpinner;
        @BindView(R.id.reps_edit_text)
        EditText repsEditText;
        @BindView(R.id.sets_edit_text)
        EditText setsEditText;
        @BindView(R.id.add_exercise_button)
        Button addExerciseButton;

        AddItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramExercise> exercises, List<String> exerciseList, ArrayAdapter spinnerAdapter, ProgramExercisesViewAdapter programExercisesViewAdapter, Context context) {
            exerciseSpinner.setAdapter(spinnerAdapter);
            setsEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addExerciseButton.performClick();
                }
                return false;
            });

            addExerciseButton.setOnClickListener(view -> {
                try {
                    addExercise(exercises, programExercisesViewAdapter);
                    resetExerciseInputFields();
                } catch (Exception exception) {
                    Toast.makeText(context, "Fill out all information!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void addExercise(List<ProgramExercise> exercises, ProgramExercisesViewAdapter programExercisesViewAdapter) {
            ProgramExercise programExercise = new ProgramExercise(exerciseSpinner.getSelectedItem().toString(), Integer.parseInt(repsEditText.getText().toString()), Integer.parseInt(setsEditText.getText().toString()));
            exercises.add(programExercise);
            programExercisesViewAdapter.notifyDataSetChanged();
        }

        private void resetExerciseInputFields() {
            exerciseSpinner.setSelection(0);
            repsEditText.setText("");
            setsEditText.setText("");
            repsEditText.requestFocus();
        }

    }

    ProgramExercisesViewAdapter(List<ProgramExercise> exercises, Context context, List<String> exerciseList) {
        this.exercises = exercises;
        this.context = context;

        spinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_item_small, exerciseList);
    }

    @Override
    public ProgramExercisesViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.view_holder_custom_workout_exercise_add_card) {
            return new ProgramExercisesViewAdapter.AddItemViewHolder(view);
        } else {
            return new ProgramExercisesViewAdapter.ShowItemViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(ProgramExercisesViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bind(exercises, userExercises, spinnerAdapter, this, context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == exercises.size())
            return R.layout.view_holder_custom_workout_exercise_add_card;
        else
            return R.layout.view_holder_custom_workout_exercise_card;
    }

    @Override
    public int getItemCount() {
        return exercises.size() + 1;
    }

}
