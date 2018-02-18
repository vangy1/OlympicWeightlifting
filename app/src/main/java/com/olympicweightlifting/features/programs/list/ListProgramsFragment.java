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

public class ListProgramsFragment extends Fragment {

    @BindView(R.id.workouts_recycler_view)
    RecyclerView workoutsRecyclerView;
    @BindView(R.id.no_workouts)
    TextView noWorkouts;

    private List<Program> programsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_premade_workouts, container, false);
        ButterKnife.bind(this, fragmentView);

        setupRecyclerView();
        populateRecyclerViewFromFirestore();


        return fragmentView;
    }


    private void setupRecyclerView() {
        workoutsRecyclerView.setHasFixedSize(true);
        workoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutsRecyclerView.setAdapter(new ListProgramsRecyclerViewAdapter(programsList, getActivity()));
    }

    private void populateRecyclerViewFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference workoutsCollection = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("programs");
        workoutsCollection.orderBy("dateAdded", Query.Direction.ASCENDING).addSnapshotListener((documentSnapshots, e) -> {
            programsList.clear();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                try {
                    Program queriedObject = documentSnapshot.toObject(Program.class).withId(documentSnapshot.getId());
//                    if (queriedObject.validateObject()) {
                    programsList.add(queriedObject);
//                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            workoutsRecyclerView.getAdapter().notifyDataSetChanged();
            handleNoWorkoutTracked();
        });
    }

    private void handleNoWorkoutTracked() {
        if (programsList.isEmpty()) {
            noWorkouts.setVisibility(View.VISIBLE);
            workoutsRecyclerView.setVisibility(View.GONE);
        } else {
            noWorkouts.setVisibility(View.GONE);
            workoutsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
