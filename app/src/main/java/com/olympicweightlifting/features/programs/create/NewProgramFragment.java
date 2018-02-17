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
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NewProgramFragment extends DaggerFragment implements ExerciseManagerDialog.OnExerciseListChangedListener {

    @BindView(R.id.weeks_recycler_view)
    RecyclerView weeksRecyclerView;
    @BindView(R.id.exercise_manager_button)
    Button exerciseManagerButton;

    @BindView(R.id.program_name_edit_text)
    EditText programNameEditText;
    @BindView(R.id.save_button)
    Button saveButton;

    @Inject
    AppDatabase database;
    List<String> exerciseList = new ArrayList<>();
    private Program program;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_custom_workout_new, container, false);
        ButterKnife.bind(this, fragmentView);

        ProgramDay programDay = new ProgramDay(new ArrayList<>(Arrays.asList()));

        ProgramWeek programWeek = new ProgramWeek(new ArrayList<>(Arrays.asList(programDay)));

        program = new Program((new ArrayList<>(Arrays.asList(programWeek))));

        setupRecyclerView();

        exerciseManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseManagerDialog exerciseManagerDialog = new ExerciseManagerDialog();
                exerciseManagerDialog.setTargetFragment(NewProgramFragment.this, 0);
                exerciseManagerDialog.show(NewProgramFragment.this.getFragmentManager(), "exerciseManagerDialog");
            }
        });

        database.exerciseDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedExercises) -> {
            for (Exercise queriedExercise : queriedExercises) {
                exerciseList.add(queriedExercise.getExerciseName());
            }
            ((ProgramWeeksViewAdapter) weeksRecyclerView.getAdapter()).notifyExercisesQueried(exerciseList);
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!programNameEditText.getText().toString().isEmpty()) {
                    program.setProgramTitle(programNameEditText.getText().toString());
                    try {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("programs").add(program);

                        program.getWeeks().clear();
                        programNameEditText.setText("");
                        weeksRecyclerView.getAdapter().notifyDataSetChanged();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong while saving your program!", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getActivity(), "Fill out program name!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return fragmentView;

    }


    private void setupRecyclerView() {
        weeksRecyclerView.setHasFixedSize(false);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        weeksRecyclerView.setLayoutManager(layout);
        weeksRecyclerView.setAdapter(new ProgramWeeksViewAdapter(program, this, layout));
    }

    @Override
    public void onExerciseAdded(String exercise) {
        exerciseList.add(exercise);
        ((ProgramWeeksViewAdapter) weeksRecyclerView.getAdapter()).notifyExerciseListModified();
    }

    @Override
    public void onExerciseRemoved(String exercise) {
        exerciseList.remove(exercise);
        ((ProgramWeeksViewAdapter) weeksRecyclerView.getAdapter()).notifyExerciseListModified();
    }
}
