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
        @BindView(R.id.layout_item)
        ViewGroup layoutItem;

        @BindView(R.id.text_exercise_name)
        TextView textViewExerciseName;
        @BindView(R.id.text_reps_value)
        TextView textViewRepsValue;
        @BindView(R.id.text_sets_value)
        TextView textViewSets;

        ShowItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramExercise> exercises, List<String> exerciseList, ArrayAdapter spinnerAdapter, ProgramExercisesViewAdapter programExercisesViewAdapter, Context context) {
            ProgramExercise currentProgramExercise = exercises.get(getAdapterPosition());
            textViewExerciseName.setText(currentProgramExercise.getExerciseName());
            textViewRepsValue.setText(String.valueOf(currentProgramExercise.getReps()));
            textViewSets.setText(String.valueOf(currentProgramExercise.getSets()));

            layoutItem.setOnLongClickListener(view -> {
                exercises.remove(getAdapterPosition());
                programExercisesViewAdapter.notifyItemRemoved(getAdapterPosition());
                return true;
            });

        }
    }

    static class AddItemViewHolder extends ProgramExercisesViewAdapter.ViewHolder {
        @BindView(R.id.spinner_exercise)
        Spinner spinnerExercise;
        @BindView(R.id.edittext_reps)
        EditText editTextReps;
        @BindView(R.id.edittext_sets)
        EditText editTextSets;
        @BindView(R.id.button_add_exercise)
        Button buttonAddExercise;

        AddItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramExercise> exercises, List<String> exerciseList, ArrayAdapter spinnerAdapter, ProgramExercisesViewAdapter programExercisesViewAdapter, Context context) {
            spinnerExercise.setAdapter(spinnerAdapter);
            editTextSets.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buttonAddExercise.performClick();
                }
                return false;
            });

            buttonAddExercise.setOnClickListener(view -> {
                try {
                    addExercise(exercises, programExercisesViewAdapter);
                    resetExerciseInputFields();
                } catch (Exception exception) {
                    Toast.makeText(context, context.getString(R.string.all_insufficient_input), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void addExercise(List<ProgramExercise> exercises, ProgramExercisesViewAdapter programExercisesViewAdapter) {
            ProgramExercise programExercise = new ProgramExercise(spinnerExercise.getSelectedItem().toString(), Integer.parseInt(editTextReps.getText().toString()), Integer.parseInt(editTextSets.getText().toString()));
            exercises.add(programExercise);
            programExercisesViewAdapter.notifyDataSetChanged();
        }

        private void resetExerciseInputFields() {
            spinnerExercise.setSelection(0);
            editTextReps.setText("");
            editTextSets.setText("");
            editTextReps.requestFocus();
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
        if (viewType == R.layout.view_holder_programs_add_exercise) {
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
            return R.layout.view_holder_programs_add_exercise;
        else
            return R.layout.view_holder_programs_exercise;
    }

    @Override
    public int getItemCount() {
        return exercises.size() + 1;
    }

}
