package com.olympicweightlifting.features.programs.list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.Program;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramsListFragment extends Fragment {

    @BindView(R.id.recyclerview_workouts)
    RecyclerView recyclerViewWorkouts;
    @BindView(R.id.text_no_programs_saved)
    TextView textViewNoProgramsSaved;

    private List<Program> programsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_programs_list, container, false);
        ButterKnife.bind(this, fragmentView);

        setupRecyclerView();
        populateRecyclerViewFromFirestore();


        return fragmentView;
    }


    private void setupRecyclerView() {
        recyclerViewWorkouts.setHasFixedSize(true);
        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewWorkouts.setAdapter(new ProgramsListRecyclerViewAdapter(programsList, getActivity()));
    }

    private void populateRecyclerViewFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference workoutsCollection = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("programs");
        workoutsCollection.orderBy("dateAdded", Query.Direction.ASCENDING).addSnapshotListener((documentSnapshots, e) -> {
            programsList.clear();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                try {
                    Program queriedObject = documentSnapshot.toObject(Program.class).withId(documentSnapshot.getId());
                    if (queriedObject.validateObject()) {
                        programsList.add(queriedObject);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            recyclerViewWorkouts.getAdapter().notifyDataSetChanged();
            handleNoWorkoutTracked();
        });
    }

    private void handleNoWorkoutTracked() {
        if (programsList.isEmpty()) {
            textViewNoProgramsSaved.setVisibility(View.VISIBLE);
            recyclerViewWorkouts.setVisibility(View.GONE);
        } else {
            textViewNoProgramsSaved.setVisibility(View.GONE);
            recyclerViewWorkouts.setVisibility(View.VISIBLE);
        }
    }

}
