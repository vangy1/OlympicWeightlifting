package com.olympicweightlifting.features.programs.create;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.helpers.exercisemanager.Exercise;
import com.olympicweightlifting.features.helpers.exercisemanager.ExerciseManagerDialog;
import com.olympicweightlifting.features.programs.data.Program;
import com.olympicweightlifting.features.programs.data.ProgramDay;
import com.olympicweightlifting.features.programs.data.ProgramWeek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ProgramCreateFragment extends DaggerFragment implements ExerciseManagerDialog.OnExerciseListChangedListener {
    @BindView(R.id.recyclerview_weeks)
    RecyclerView recyclerViewWeeks;
    @BindView(R.id.button_exercise_manager)
    Button buttonExerciseManager;
    @BindView(R.id.edittext_program_name)
    EditText editTextProgramName;
    @BindView(R.id.button_save)
    Button buttonSave;

    @Inject
    AppDatabase database;

    private List<String> userExercises = new ArrayList<>();
    private Program program;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_programs_create, container, false);
        ButterKnife.bind(this, fragmentView);

        program = new Program(new ArrayList<>(Collections.singletonList(new ProgramWeek(new ArrayList<>(Collections.singletonList(new ProgramDay()))))));

        setupRecyclerView();

        buttonExerciseManager.setOnClickListener(view -> {
            ExerciseManagerDialog exerciseManagerDialog = new ExerciseManagerDialog();
            exerciseManagerDialog.setTargetFragment(ProgramCreateFragment.this, 0);
            exerciseManagerDialog.show(ProgramCreateFragment.this.getFragmentManager(), "exerciseManagerDialog");
        });

        database.exerciseDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedExercises) -> {
            for (Exercise queriedExercise : queriedExercises) {
                userExercises.add(queriedExercise.getExerciseName());
            }
            ((ProgramWeeksViewAdapter) recyclerViewWeeks.getAdapter()).notifyExercisesQueried(userExercises);
        });

        buttonSave.setOnClickListener(view -> {
            if (!editTextProgramName.getText().toString().isEmpty()) {
                program.setProgramTitle(editTextProgramName.getText().toString());
                try {
                    saveProgramToFirestore();
                    Toast.makeText(getActivity(), editTextProgramName.getText().toString() + " program was saved", Toast.LENGTH_SHORT).show();
                    clearOldProgram();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Toast.makeText(getActivity(), "Something went wrong while saving your program!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Fill out program name!", Toast.LENGTH_SHORT).show();
            }

        });

        return fragmentView;

    }

    private void setupRecyclerView() {
        recyclerViewWeeks.setHasFixedSize(false);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        recyclerViewWeeks.setLayoutManager(layout);
        recyclerViewWeeks.setAdapter(new ProgramWeeksViewAdapter(program, getActivity().getBaseContext(), layout));
    }

    private void saveProgramToFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("programs").add(program);
    }

    private void clearOldProgram() {
        program.getWeeks().clear();
        program.getWeeks().add(new ProgramWeek(new ArrayList<>(Collections.singletonList(new ProgramDay()))));
        editTextProgramName.setText("");
        recyclerViewWeeks.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onExerciseAdded(String exercise) {
        userExercises.add(exercise);
        ((ProgramWeeksViewAdapter) recyclerViewWeeks.getAdapter()).notifyExerciseListModified();
    }

    @Override
    public void onExerciseRemoved(String exercise) {
        userExercises.remove(exercise);
        ((ProgramWeeksViewAdapter) recyclerViewWeeks.getAdapter()).notifyExerciseListModified();
    }
}
