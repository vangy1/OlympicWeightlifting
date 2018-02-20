package com.olympicweightlifting.features.tracking.tracknew;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.helpers.exercisemanager.Exercise;
import com.olympicweightlifting.features.helpers.exercisemanager.ExerciseManagerDialog;
import com.olympicweightlifting.features.tracking.data.TrackedExerciseData;
import com.olympicweightlifting.features.tracking.data.TrackedWorkoutData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.text.format.DateFormat.getDateFormat;
import static com.olympicweightlifting.utilities.ApplicationConstants.*;
import static com.olympicweightlifting.utilities.ApplicationConstants.Units;

public class TrackingNewFragment extends DaggerFragment implements DatePickerDialog.OnDateSetListener, ExerciseManagerDialog.OnExerciseListChangedListener {

    @BindView(R.id.exercises_recycler_view)
    RecyclerView exercisesRecyclerView;

    @BindView(R.id.edittext_weight)
    EditText editTextWeight;
    @BindView(R.id.text_weight_units)
    TextView textWeightUnits;
    @BindView(R.id.edittext_sets)
    EditText editTextReps;
    @BindView(R.id.edittext_reps)
    EditText editTextSets;
    @BindView(R.id.spinner_exercise)
    Spinner spinnerExercise;
    @BindView(R.id.button_exercise_manager)
    ImageButton buttonExerciseManager;
    @BindView(R.id.button_add)
    Button buttonAdd;
    @BindView(R.id.button_date_picker)
    Button buttonDatePicker;
    @BindView(R.id.button_save)
    Button buttonSave;


    List<String> exerciseList = new ArrayList<>();
    ArrayAdapter spinnerAdapter;
    @Inject
    AppDatabase database;
    @Inject
    @Named("settings")
    SharedPreferences sharedPreferences;

    private DateFormat dateFormat;
    private Date currentDate;
    private List<TrackedExerciseData> trackedExerciseData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_tracking_new, container, false);
        ButterKnife.bind(this, fragmentView);
        dateFormat = getDateFormat(getActivity());
        textWeightUnits.setText(sharedPreferences.getString(PREF_UNITS, Units.KG.toString()).toLowerCase());

        setupDatePicker();
        setupSpinner();
        setupRecyclerView();

        buttonExerciseManager.setOnClickListener(view -> {
            ExerciseManagerDialog exerciseManagerDialog = new ExerciseManagerDialog();
            exerciseManagerDialog.setTargetFragment(this, 0);
            exerciseManagerDialog.show(getFragmentManager(), ExerciseManagerDialog.TAG);
        });

        buttonAdd.setOnClickListener(view -> {
            try {
                trackedExerciseData.add(0, new TrackedExerciseData(Double.parseDouble(editTextWeight.getText().toString()), Units.KG.toString(),
                        Integer.parseInt(editTextReps.getText().toString()), Integer.parseInt(editTextSets.getText().toString()), spinnerExercise.getSelectedItem().toString()));
                exercisesRecyclerView.getAdapter().notifyItemInserted(0);
                exercisesRecyclerView.scrollToPosition(0);
            } catch (Exception exception) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.all_insufficient_input), Toast.LENGTH_SHORT).show();
            }

        });

        buttonSave.setOnClickListener(view -> {
            saveWorkoutToFirestore();
            Toast.makeText(getActivity(), getActivity().getString(R.string.tracking_workout_saved), Toast.LENGTH_SHORT).show();
            clearInputData();
        });

        return fragmentView;
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        buttonDatePicker.setText(dateFormat.format(calendar.getTime()));
        buttonDatePicker.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_exercise, exerciseList);
        spinnerExercise.setAdapter(spinnerAdapter);

        database.exerciseDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedExercises) -> {
            for (Exercise queriedExercise : queriedExercises) {
                exerciseList.add(queriedExercise.getExerciseName());
            }
            spinnerAdapter.notifyDataSetChanged();
        });
    }

    private void setupRecyclerView() {
        exercisesRecyclerView.setHasFixedSize(false);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exercisesRecyclerView.setAdapter(new TrackingNewRecyclerViewAdapter(trackedExerciseData, getActivity()));
    }

    private void saveWorkoutToFirestore() {
        TrackedWorkoutData trackedWorkoutData = new TrackedWorkoutData(trackedExerciseData, currentDate);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection(FIREBASE_COLLECTION_USERS).document(currentUser.getUid()).collection(FIREBASE_COLLECTION_WORKOUTS_TRACKED).add(trackedWorkoutData);
    }

    private void clearInputData() {
        trackedExerciseData.clear();
        exercisesRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1;
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(day + "/" + month + "/" + year);
            currentDate = date;
            buttonDatePicker.setText(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onExerciseAdded(String exercise) {
        this.exerciseList.add(exercise);
        spinnerAdapter.notifyDataSetChanged();
        spinnerExercise.setSelection(exerciseList.size());

    }

    @Override
    public void onExerciseRemoved(String exercise) {
        this.exerciseList.remove(exercise);
        spinnerAdapter.notifyDataSetChanged();
    }
}
