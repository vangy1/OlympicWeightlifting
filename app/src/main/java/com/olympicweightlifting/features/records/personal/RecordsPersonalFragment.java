package com.olympicweightlifting.features.records.personal;


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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.helpers.exercisemanager.Exercise;
import com.olympicweightlifting.features.helpers.exercisemanager.ExerciseManagerDialog;

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


public class RecordsPersonalFragment extends DaggerFragment implements DatePickerDialog.OnDateSetListener, ExerciseManagerDialog.OnExerciseListChangedListener {
    @BindView(R.id.recyclerview_records)
    RecyclerView recordsRecyclerView;

    @BindView(R.id.edittext_weight)
    EditText editTextWeight;
    @BindView(R.id.text_weight_units)
    TextView textWeightUnits;
    @BindView(R.id.edittext_reps)
    EditText editTextReps;
    @BindView(R.id.spinner_exercise)
    Spinner spinnerExercise;
    @BindView(R.id.button_exercise_manager)
    ImageButton buttonExerciseManager;
    @BindView(R.id.button_date_picker)
    Button buttonDatePicker;
    @BindView(R.id.button_save)
    Button buttonSave;

    @Inject
    AppDatabase database;
    @Inject
    @Named("settings")
    SharedPreferences sharedPreferences;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    List<String> exerciseList = new ArrayList<>();
    ArrayAdapter spinnerAdapter;
    private DateFormat dateFormat;
    private List<RecordsPersonalData> recordsPersonalDataList = new ArrayList<>();
    private Date currentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_records_personal, container, false);
        ButterKnife.bind(this, fragmentView);
        dateFormat = getDateFormat(getActivity());
        String units = sharedPreferences.getString(PREF_UNITS, Units.KG.toString());

        textWeightUnits.setText(units.toLowerCase());
        setupSpinner();
        setupDatePicker();
        setupRecyclerView();
        populateRecyclerViewFromFirestore();

        buttonExerciseManager.setOnClickListener(view -> {
            ExerciseManagerDialog exerciseManagerDialog = new ExerciseManagerDialog();
            exerciseManagerDialog.setTargetFragment(this, 0);
            exerciseManagerDialog.show(getFragmentManager(), ExerciseManagerDialog.TAG);
        });

        buttonSave.setOnClickListener(view -> {
            try {
                RecordsPersonalData recordsPersonalData = new RecordsPersonalData(Double.parseDouble(editTextWeight.getText().toString()), units,
                        Integer.parseInt(editTextReps.getText().toString()), spinnerExercise.getSelectedItem().toString(), currentDate);
                FirebaseFirestore.getInstance().collection(FIREBASE_COLLECTION_USERS).document(currentUser.getUid()).collection(FIREBASE_COLLECTION_RECORDS_PERSONAL).add(recordsPersonalData);
            } catch (Exception exception) {
                Toast.makeText(getActivity(), getString(R.string.all_insufficient_input), Toast.LENGTH_SHORT).show();
            }
        });

        return fragmentView;
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

    private void setupRecyclerView() {
        recordsRecyclerView.setHasFixedSize(false);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordsRecyclerView.setAdapter(new RecordsPersonalRecyclerViewAdapter(recordsPersonalDataList, getActivity()));
    }

    private void populateRecyclerViewFromFirestore() {
        CollectionReference personalRecordsCollection = FirebaseFirestore.getInstance().collection(FIREBASE_COLLECTION_USERS).document(currentUser.getUid()).collection(FIREBASE_COLLECTION_RECORDS_PERSONAL);
        personalRecordsCollection.orderBy("dateAdded", Query.Direction.DESCENDING).addSnapshotListener((documentSnapshots, e) -> {
            recordsPersonalDataList.clear();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                try {
                    RecordsPersonalData queriedObject = documentSnapshot.toObject(RecordsPersonalData.class).withId(documentSnapshot.getId());
                    if (queriedObject.validateObject()) {
                        recordsPersonalDataList.add(queriedObject);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            recordsRecyclerView.getAdapter().notifyDataSetChanged();
        });
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
