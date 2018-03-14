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
import dagger.android.support.DaggerAppCompatDialogFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExerciseManagerDialog extends DaggerAppCompatDialogFragment {
    public static final String TAG = ExerciseManagerDialog.class.getCanonicalName();

    @BindView(R.id.text_dialog_title)
    TextView textViewDialogTitle;
    @BindView(R.id.edittext_exercise_to_add)
    EditText editTextExerciseToAdd;
    @BindView(R.id.button_add_exercise)
    Button buttonAddExercise;
    @BindView(R.id.spinner_exercise_to_delete)
    Spinner spinnerExerciseToDelete;
    @BindView(R.id.button_remove_exercise)
    Button buttonRemoveExercise;
    @BindView(R.id.button_done)
    Button buttonDone;

    ArrayAdapter spinnerAdapter;
    List<String> exerciseList = new ArrayList<>();

    OnExerciseListChangedListener onExerciseListChangedListener;

    @Inject
    AppDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onExerciseListChangedListener = (OnExerciseListChangedListener) getTargetFragment();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_exercise_manager, null);
        ButterKnife.bind(this, dialogView);

        textViewDialogTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold)));

        setupExerciseSpinner();
        populateExerciseSpinner();


        buttonAddExercise.setOnClickListener(view -> {
            if (!editTextExerciseToAdd.getText().toString().isEmpty()) {
                String exerciseName = Character.toUpperCase(editTextExerciseToAdd.getText().toString().charAt(0)) + editTextExerciseToAdd.getText().toString().substring(1);
                Exercise exercise = new Exercise(exerciseName);
                Completable.fromAction(() -> database.exerciseDao().insert(exercise)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> addExercise(exercise)).onErrorComplete().subscribe();
            }
        });


        buttonRemoveExercise.setOnClickListener(view -> {
            if (spinnerExerciseToDelete.getSelectedItemPosition() != -1) {
                String exerciseName = (String) spinnerAdapter.getItem(spinnerExerciseToDelete.getSelectedItemPosition());
                Completable.fromAction(() -> database.exerciseDao().deleteByExerciseName(exerciseName)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> removeExercise(exerciseName)).onErrorComplete().subscribe();
            }
        });

        buttonDone.setOnClickListener(view -> {
            this.dismiss();
        });


        return new AlertDialog.Builder(getActivity()).setView(dialogView).create();
    }

    private void setupExerciseSpinner() {
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, exerciseList);
        spinnerExerciseToDelete.setAdapter(spinnerAdapter);
    }

    private void populateExerciseSpinner() {
        database.exerciseDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedExercises) -> {
            for (Exercise queriedExercise : queriedExercises) {
                exerciseList.add(queriedExercise.getExerciseName());
            }
            spinnerAdapter.notifyDataSetChanged();
        });

    }

    private void addExercise(Exercise exercise) {
        exerciseList.add(exercise.getExerciseName());
        spinnerExerciseToDelete.setSelection(exerciseList.size());
        spinnerAdapter.notifyDataSetChanged();
        onExerciseListChangedListener.onExerciseAdded(exercise.getExerciseName());
    }

    private void removeExercise(String exerciseName) {
        exerciseList.remove(exerciseName);
        spinnerAdapter.notifyDataSetChanged();
        onExerciseListChangedListener.onExerciseRemoved(exerciseName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }


    public interface OnExerciseListChangedListener {
        void onExerciseAdded(String exercise);

        void onExerciseRemoved(String exercise);
    }
}
