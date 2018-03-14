package com.olympicweightlifting.features.programs.create;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.billingclient.api.Purchase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.olympicweightlifting.App;
import com.olympicweightlifting.R;
import com.olympicweightlifting.billing.BillingListener;
import com.olympicweightlifting.billing.BillingManager;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.helpers.exercisemanager.Exercise;
import com.olympicweightlifting.features.helpers.exercisemanager.ExerciseManagerDialog;
import com.olympicweightlifting.features.programs.data.Program;
import com.olympicweightlifting.features.programs.data.ProgramDay;
import com.olympicweightlifting.features.programs.data.ProgramWeek;
import com.olympicweightlifting.utilities.ApplicationHelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_PROGRAMS;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_USERS;
import static com.olympicweightlifting.utilities.ApplicationConstants.PROGRAMS_LIMIT;
import static com.olympicweightlifting.utilities.ApplicationConstants.UserProfileStatus;


public class ProgramCreateFragment extends DaggerFragment implements ExerciseManagerDialog.OnExerciseListChangedListener {
    @BindView(R.id.layout_fragment)
    ConstraintLayout layoutFragment;
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

    private BillingManager billingManager;
    private UserProfileStatus userProfileStatus = UserProfileStatus.UNDEFINED;
    private int programsTracked = -1;
    private ListenerRegistration programsRealtimeListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_programs_create, container, false);
        ButterKnife.bind(this, fragmentView);
        getUserProfileStatus();
        getNumberOfTrackedPrograms();

        program = new Program(new ArrayList<>(Collections.singletonList(new ProgramWeek(new ArrayList<>(Collections.singletonList(new ProgramDay()))))));

        setupRecyclerView();

        buttonExerciseManager.setOnClickListener(view -> openExerciseManagerDialog());

        database.exerciseDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedExercises) -> {
            for (Exercise queriedExercise : queriedExercises) {
                userExercises.add(queriedExercise.getExerciseName());
            }
            ((ProgramWeeksViewAdapter) recyclerViewWeeks.getAdapter()).notifyExercisesQueried(userExercises);
        });

        buttonSave.setOnClickListener(view -> {
            if (checkIfUserReachedTheLimit()) {
                if (!editTextProgramName.getText().toString().isEmpty()) {
                    program.setProgramTitle(editTextProgramName.getText().toString());
                    try {
                        saveProgramToFirestore();
                        Toast.makeText(getActivity(), editTextProgramName.getText().toString() + getString(R.string.programs_saved), Toast.LENGTH_SHORT).show();
                        clearInputData();
                        ((App) getActivity().getApplication()).getAnalyticsTracker().sendEvent("Programs Activity", "Save program");
                    } catch (Exception exception) {
                        Toast.makeText(getActivity(), R.string.programs_error_while_saving, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.programs_empty_name, Toast.LENGTH_SHORT).show();
                }
            } else {
                ApplicationHelpers.hideKeyboard(getActivity(), view);
                billingManager.promptUserToUpgrade(getActivity(), layoutFragment);
            }
        });

        return fragmentView;

    }

    private void getUserProfileStatus() {
        billingManager = new BillingManager(getActivity(), new BillingListener() {
            @Override
            public void onPurchasesQueried(List<Purchase> purchases) {
                if (billingManager.isUserPremium(purchases)) {
                    userProfileStatus = UserProfileStatus.PREMIUM;
                } else {
                    userProfileStatus = UserProfileStatus.FREE;
                }
            }

            @Override
            public void onPurchasesUpdated(List<Purchase> purchases) {
                onPurchasesQueried(purchases);
            }
        });
    }

    private void getNumberOfTrackedPrograms() {
        programsRealtimeListener = FirebaseFirestore.getInstance()
                .collection(FIREBASE_COLLECTION_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection(FIREBASE_COLLECTION_PROGRAMS)
                .addSnapshotListener((documentSnapshots, e) -> {
                    int validPrograms = 0;
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.toObject(Program.class).withId(documentSnapshot.getId()).validateObject()) {
                            validPrograms += 1;
                        }
                    }
                    programsTracked = validPrograms;
                });
    }

    private void setupRecyclerView() {
        recyclerViewWeeks.setHasFixedSize(false);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        recyclerViewWeeks.setLayoutManager(layout);
        recyclerViewWeeks.setAdapter(new ProgramWeeksViewAdapter(program, getActivity().getBaseContext(), layout));
    }

    private void openExerciseManagerDialog() {
        ExerciseManagerDialog exerciseManagerDialog = new ExerciseManagerDialog();
        exerciseManagerDialog.setTargetFragment(ProgramCreateFragment.this, 0);
        exerciseManagerDialog.show(ProgramCreateFragment.this.getFragmentManager(), ExerciseManagerDialog.TAG);
    }

    private boolean checkIfUserReachedTheLimit() {
        return programsTracked != -1 && programsTracked < (userProfileStatus == UserProfileStatus.PREMIUM ? Integer.MAX_VALUE : PROGRAMS_LIMIT);
    }

    private void saveProgramToFirestore() {
        program.setDateAdded();
        FirebaseFirestore.getInstance().collection(FIREBASE_COLLECTION_USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(FIREBASE_COLLECTION_PROGRAMS).add(program);
    }

    private void clearInputData() {
        program.getWeeks().clear();
        program.getWeeks().add(new ProgramWeek(new ArrayList<>(Collections.singletonList(new ProgramDay()))));
        editTextProgramName.setText("");
        recyclerViewWeeks.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        if (billingManager != null) billingManager.destroy();
        if (programsRealtimeListener != null) programsRealtimeListener.remove();
        super.onDestroyView();
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
