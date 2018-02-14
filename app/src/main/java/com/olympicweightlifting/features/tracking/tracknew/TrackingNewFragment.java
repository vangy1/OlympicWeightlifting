package com.olympicweightlifting.features.tracking.tracknew;


import android.app.DatePickerDialog;
import android.app.Fragment;
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
import com.olympicweightlifting.features.tracking.TrackedExerciseData;
import com.olympicweightlifting.features.tracking.TrackedWorkoutData;

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
import dagger.android.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.text.format.DateFormat.getDateFormat;
import static com.olympicweightlifting.utilities.AppLevelConstants.Units;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackingNewFragment extends DaggerFragment implements DatePickerDialog.OnDateSetListener, ExerciseManagerDialog.OnExerciseListChangedListener {

    @BindView(R.id.exercises_recycler_view)
    RecyclerView exercisesRecyclerView;

    @BindView(R.id.weight_edit_text)
    EditText weightEditText;
    @BindView(R.id.weight_units)
    TextView weightUnits;
    @BindView(R.id.reps_edit_text)
    EditText repsEditText;
    @BindView(R.id.sets_edit_text)
    EditText setsEditText;
    @BindView(R.id.exercise_spinner)
    Spinner exerciseSpinner;
    @BindView(R.id.exercise_manager_button)
    ImageButton exerciseManagerButton;
    @BindView(R.id.add_button)
    Button addButton;
    @BindView(R.id.date_picker_button)
    Button datePickerButton;
    @BindView(R.id.save_button)
    Button saveButton;


    List<String> exerciseList = new ArrayList<>();
    ArrayAdapter spinnerAdapter;
    @Inject
    AppDatabase database;
    @Inject
    @Named("settings")
    SharedPreferences sharedPreferences;
    private DateFormat dateFormat = getDateFormat(getActivity());
    private Date currentDate;
    private List<TrackedExerciseData> trackedExerciseData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_tracking_new, container, false);
        ButterKnife.bind(this, fragmentView);

        setupDatePicker();
        setupSpinner();
        setupRecyclerView();

        exerciseManagerButton.setOnClickListener(view -> {
            ExerciseManagerDialog exerciseManagerDialog = new ExerciseManagerDialog();
            exerciseManagerDialog.setTargetFragment(this, 0);
            exerciseManagerDialog.show(getFragmentManager(), "exerciseManagerDialog");
        });

        addButton.setOnClickListener(view -> {
            try {
                trackedExerciseData.add(new TrackedExerciseData(Double.parseDouble(weightEditText.getText().toString()), Units.KG.toString(),
                        Integer.parseInt(repsEditText.getText().toString()), Integer.parseInt(setsEditText.getText().toString()), exerciseSpinner.getSelectedItem().toString()));
                exercisesRecyclerView.getAdapter().notifyDataSetChanged();
            } catch (Exception exception) {
                Toast.makeText(getActivity(), "Fill out all information!", Toast.LENGTH_SHORT).show();
            }

        });

        saveButton.setOnClickListener(view -> {
            saveWorkoutToFirestore();
            Toast.makeText(getActivity(), "Workout has been saved!", Toast.LENGTH_SHORT).show();
            clearInputData();
        });

        return fragmentView;
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        datePickerButton.setText(dateFormat.format(calendar.getTime()));
        datePickerButton.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_exercise, exerciseList);
        exerciseSpinner.setAdapter(spinnerAdapter);

        database.exerciseDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedExercises) -> {
            for (Exercise queriedExercise : queriedExercises) {
                exerciseList.add(queriedExercise.getExerciseName());
            }
            spinnerAdapter.notifyDataSetChanged();
        });
    }

    private void setupRecyclerView() {
        exercisesRecyclerView.setHasFixedSize(true);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exercisesRecyclerView.setAdapter(new TrackingNewRecyclerViewAdapter(trackedExerciseData, getActivity()));
    }

    private void saveWorkoutToFirestore() {
        TrackedWorkoutData trackedWorkoutData = new TrackedWorkoutData(trackedExerciseData, currentDate);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("tracked_workouts").add(trackedWorkoutData);
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
            datePickerButton.setText(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onExerciseAdded(String exercise) {
        this.exerciseList.add(exercise);
        spinnerAdapter.notifyDataSetChanged();
        exerciseSpinner.setSelection(exerciseList.size());

    }

    @Override
    public void onExerciseRemoved(String exercise) {
        this.exerciseList.remove(exercise);
        spinnerAdapter.notifyDataSetChanged();
    }
}
