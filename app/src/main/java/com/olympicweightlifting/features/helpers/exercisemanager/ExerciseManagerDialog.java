package com.olympicweightlifting.features.helpers.exercisemanager;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerDialogFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExerciseManagerDialog extends DaggerDialogFragment {
    @BindView(R.id.dialog_title)
    TextView dialogTitle;
    @BindView(R.id.exercise_to_add)
    EditText exerciseToAdd;
    @BindView(R.id.add_exercise)
    Button addExercise;
    @BindView(R.id.exercise_to_delete)
    Spinner exerciseToDelete;
    @BindView(R.id.delete_exercise)
    Button deleteExercise;

    ArrayAdapter spinnerAdapter;
    List<String> exerciseList = new ArrayList<>();

    @Inject
    AppDatabase database;

    OnExerciseListChangedListener onExerciseListChangedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onExerciseListChangedListener = (OnExerciseListChangedListener) getTargetFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_exercises_manager, null);
        ButterKnife.bind(this, dialogView);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialogTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold)));

        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, exerciseList);
        exerciseToDelete.setAdapter(spinnerAdapter);


        addExercise.setOnClickListener(view -> {
            String exerciseName = exerciseToAdd.getText().toString();
            if (!exerciseName.isEmpty()) {
                Exercise exercise = new Exercise(exerciseName);

                Completable.fromAction(() -> {
                    database.exerciseDao().insert(exercise);
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> {
                    exerciseList.add(exercise.getExerciseName());
                    exerciseToDelete.setSelection(exerciseList.size());
                    spinnerAdapter.notifyDataSetChanged();
                    onExerciseListChangedListener.onExerciseAdded(exercise.getExerciseName());
                }).onErrorComplete().subscribe();
            }
        });

        database.exerciseDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedExercises) -> {
            for (Exercise queriedExercise : queriedExercises) {
                exerciseList.add(queriedExercise.getExerciseName());
            }
            spinnerAdapter.notifyDataSetChanged();
        });

        deleteExercise.setOnClickListener(view -> {
            if (exerciseToDelete.getSelectedItemPosition() != -1) {
                String exerciseName = (String) spinnerAdapter.getItem(exerciseToDelete.getSelectedItemPosition());
                Completable.fromAction(() -> {
                    database.exerciseDao().deleteByExerciseName(exerciseName);
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> {
                    exerciseList.remove(exerciseName);
                    spinnerAdapter.notifyDataSetChanged();
                    onExerciseListChangedListener.onExerciseRemoved(exerciseName);
                }).onErrorComplete().subscribe();
            }
        });


        return new AlertDialog.Builder(getActivity()).setView(dialogView).setPositiveButton("Done", null).create();
    }

    public interface OnExerciseListChangedListener {
        void onExerciseAdded(String exercise);

        void onExerciseRemoved(String exercise);
    }

}
