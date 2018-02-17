package com.olympicweightlifting.features.programs.create;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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

import static com.olympicweightlifting.utilities.AppLevelConstants.VIEW_HOLDER_ITEM_LAST;
import static com.olympicweightlifting.utilities.AppLevelConstants.VIEW_HOLDER_ITEM_NORMAL;

/**
 * Created by vangor on 01/02/2018.
 */

public class ProgramExercisesViewAdapter extends RecyclerView.Adapter<ProgramExercisesViewAdapter.ViewHolder> {
    private final Fragment parentFragment;
    List<String> exerciseList = new ArrayList<>();
    ArrayAdapter spinnerAdapter;
    private List<ProgramExercise> exercises;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.exercise_name)
        TextView exerciseName;
        @Nullable
        @BindView(R.id.reps_value)
        TextView reps;
        @Nullable
        @BindView(R.id.sets_value)
        TextView sets;
        @Nullable
        @BindView(R.id.add_exercise_button)
        Button addExerciseButton;
        @Nullable
        @BindView(R.id.exercise_spinner)
        Spinner exerciseSpinner;

        @Nullable
        @BindView(R.id.reps_edit_text)
        EditText repsEditText;
        @Nullable
        @BindView(R.id.sets_edit_text)
        EditText setsEditText;

        int viewType;

        View view;

        ViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
            this.viewType = viewType;
        }
    }

    public ProgramExercisesViewAdapter(List<ProgramExercise> exercises, Fragment parentFragment, List<String> exerciseList) {
        this.exercises = exercises;
        this.parentFragment = parentFragment;

        spinnerAdapter = new ArrayAdapter<>(parentFragment.getActivity(), R.layout.spinner_item_small, exerciseList);
    }

    @Override
    public ProgramExercisesViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOLDER_ITEM_LAST) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_holder_custom_workout_exercise_add_card, parent, false);
            return new ProgramExercisesViewAdapter.ViewHolder(view, VIEW_HOLDER_ITEM_LAST);

        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_holder_custom_workout_exercise_card, parent, false);
            return new ProgramExercisesViewAdapter.ViewHolder(view, VIEW_HOLDER_ITEM_NORMAL);
        }
    }

    @Override
    public void onBindViewHolder(ProgramExercisesViewAdapter.ViewHolder viewHolder, int position) {

        if (viewHolder.viewType == VIEW_HOLDER_ITEM_NORMAL) {
            ProgramExercise currentProgramExercise = exercises.get(position);
            viewHolder.exerciseName.setText(currentProgramExercise.getExerciseName());
            viewHolder.reps.setText(String.valueOf(currentProgramExercise.getReps()));
            viewHolder.sets.setText(String.valueOf(currentProgramExercise.getSets()));

            viewHolder.view.setOnLongClickListener(view -> {
                exercises.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
                return true;
            });
        } else {
            viewHolder.exerciseSpinner.setAdapter(spinnerAdapter);
            viewHolder.setsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        viewHolder.addExerciseButton.callOnClick();
                        return true;
                    }
                    return false;
                }
            });

            viewHolder.addExerciseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ProgramExercise programExercise = new ProgramExercise(viewHolder.exerciseSpinner.getSelectedItem().toString(), Integer.parseInt(viewHolder.repsEditText.getText().toString()), Integer.parseInt(viewHolder.setsEditText.getText().toString()));
                        exercises.add(programExercise);
                        notifyDataSetChanged();
                        viewHolder.exerciseSpinner.setSelection(0);
                        viewHolder.repsEditText.setText("");
                        viewHolder.setsEditText.setText("");
                    } catch (Exception exception) {
                        Toast.makeText(parentFragment.getActivity(), "Fill out all information!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == exercises.size())
            return VIEW_HOLDER_ITEM_LAST;
        else
            return VIEW_HOLDER_ITEM_NORMAL;
    }

    @Override
    public int getItemCount() {
        return exercises.size() + 1;
    }

}
