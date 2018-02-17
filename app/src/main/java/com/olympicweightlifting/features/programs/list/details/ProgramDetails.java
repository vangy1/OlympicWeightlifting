package com.olympicweightlifting.features.programs.list.details;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.Program;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramDetails extends Fragment {
    @BindView(R.id.workout_date)
    TextView workoutDate;
    @BindView(R.id.workouts_recycler_view)
    RecyclerView workoutsRecyclerView;
    @BindView(R.id.delete_floating_button)
    FloatingActionButton deleteWorkoutFloatingButton;

    private Program program;

    public static ProgramDetails newInstance(Bundle fragmentArugments) {
        ProgramDetails programDetails = new ProgramDetails();
        programDetails.setArguments(fragmentArugments);
        return programDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_program_details, container, false);
        ButterKnife.bind(this, fragmentView);

        program = new Gson().fromJson(getArguments().getString("programDetails"), new TypeToken<Program>() {
        }.getType());
        workoutDate.setText(program.getProgramTitle());
        setupRecyclerView();

        deleteWorkoutFloatingButton.setOnClickListener(view -> {
            removeWorkoutFromFirestore();
            getActivity().getFragmentManager().beginTransaction()
                    .remove(ProgramDetails.this)
                    .commit();

        });

        return fragmentView;
    }

    private void setupRecyclerView() {
        workoutsRecyclerView.setHasFixedSize(true);
        workoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutsRecyclerView.setAdapter(new ProgramDetailsWeeksViewAdapter(program, getActivity().getBaseContext()));
    }

    private void removeWorkoutFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference programs = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("programs");
        programs.document(program.getDocumentId()).delete();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getFragmentManager().popBackStack();
        hideFragmentContainer();
    }

    private void hideFragmentContainer() {
        View workoutDetailsFragmentContainer = getActivity().findViewById(R.id.workout_details_fragment_container);
        workoutDetailsFragmentContainer.setVisibility(View.GONE);
    }
}
