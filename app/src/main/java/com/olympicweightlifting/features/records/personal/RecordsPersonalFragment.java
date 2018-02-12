package com.olympicweightlifting.features.records.personal;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
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
import com.mikepenz.itemanimators.SlideRightAlphaAnimator;
import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.helpers.exercisemanager.Exercise;
import com.olympicweightlifting.features.helpers.exercisemanager.ExerciseManagerDialog;
import com.olympicweightlifting.utilities.AppLevelConstants;
import com.olympicweightlifting.utilities.EditTextInputFilter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.text.format.DateFormat.getDateFormat;


public class RecordsPersonalFragment extends DaggerFragment implements DatePickerDialog.OnDateSetListener, ExerciseManagerDialog.OnExerciseListChangedListener {
    @BindView(R.id.records_recycler_view)
    RecyclerView recordsRecyclerView;

    @BindView(R.id.weight_edit_text)
    EditText weightEditText;
    @BindView(R.id.weight_units)
    TextView weightUnits;
    @BindView(R.id.reps_edit_text)
    EditText repsEditText;
    @BindView(R.id.exercise_spinner)
    Spinner exerciseSpinner;
    @BindView(R.id.exercise_manager_button)
    ImageButton exerciseManagerButton;
    @BindView(R.id.date_picker_button)
    Button datePickerButton;
    @BindView(R.id.save_button)
    Button saveButton;

    @Inject
    AppDatabase database;

    List<String> exerciseList = new ArrayList<>();
    ArrayAdapter spinnerAdapter;
    private List<PersonalRecordData> personalRecordDataList = new ArrayList<>();
    private DateFormat dateFormat = getDateFormat(getActivity());
    private Date currentDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_records_personal, container, false);
        ButterKnife.bind(this, fragmentView);

        spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_exercise, exerciseList);
        exerciseSpinner.setAdapter(spinnerAdapter);

        setupSpinner();
        setupRecyclerView();
        setupDatePicker();

        weightEditText.setFilters(new InputFilter[]{new EditTextInputFilter(1, 9999)});
        repsEditText.setFilters(new InputFilter[]{new EditTextInputFilter(1, 9999)});


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference worldRecords = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("personal_records");
        worldRecords.orderBy("dateAdded", Query.Direction.DESCENDING).addSnapshotListener((documentSnapshots, e) -> {
            personalRecordDataList.clear();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                PersonalRecordData queriedObject = documentSnapshot.toObject(PersonalRecordData.class).withId(documentSnapshot.getId());
                if (queriedObject.validateObject()) {
                    personalRecordDataList.add(queriedObject);
                }
            }
            recordsRecyclerView.getAdapter().notifyDataSetChanged();

        });

        exerciseManagerButton.setOnClickListener(view -> {
            ExerciseManagerDialog exerciseManagerDialog = new ExerciseManagerDialog();
            exerciseManagerDialog.setTargetFragment(this, 0);
            exerciseManagerDialog.show(getFragmentManager(), "exerciseManagerDialog");
        });

        saveButton.setOnClickListener(view -> {
            if (isInputValid()) {
                PersonalRecordData personalRecordData = new PersonalRecordData(Double.parseDouble(weightEditText.getText().toString()), AppLevelConstants.Units.KG.toString(),
                        Integer.parseInt(repsEditText.getText().toString()), exerciseSpinner.getSelectedItem().toString(), currentDate);
                FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("personal_records").add(personalRecordData);
            } else {
                Toast.makeText(getActivity(), "Fill out all information!", Toast.LENGTH_SHORT).show();
            }

        });

        return fragmentView;


    }


    private void setupSpinner() {
        database.exerciseDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedExercises) -> {
            for (Exercise queriedExercise : queriedExercises) {
                exerciseList.add(queriedExercise.getExerciseName());
            }
            spinnerAdapter.notifyDataSetChanged();
        });

    }

    private void setupRecyclerView() {
        recordsRecyclerView.setHasFixedSize(true);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordsRecyclerView.setAdapter(new RecordsPersonalRecyclerViewAdapter(personalRecordDataList, getActivity()));
        recordsRecyclerView.setItemAnimator(new SlideRightAlphaAnimator());
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

    public boolean isInputValid() {
        return weightEditText.getText().length() != 0 &&
                repsEditText.getText().length() != 0 &&
                exerciseSpinner.getSelectedItem() != null &&
                datePickerButton.getText().length() != 0;
    }

//    @Override
//    public void onExerciseListChasnged(List<String> exerciseList) {
//        this.exerciseList.clear();
//        this.exerciseList.addAll(exerciseList);
//        spinnerAdapter.notifyDataSetChanged();
//    }

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
