package com.olympicweightlifting.features.tracking.history;


import android.app.Fragment;
import android.os.Bundle;
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
import com.olympicweightlifting.features.tracking.TrackedWorkoutData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrackingHistoryFragment extends Fragment {


    @BindView(R.id.workouts_recycler_view)
    RecyclerView workoutsRecyclerView;
    @BindView(R.id.no_workouts)
    TextView noWorkouts;

    private List<TrackedWorkoutData> trackedWorkouts = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_tracking_history, container, false);
        ButterKnife.bind(this, fragmentView);

        setupRecyclerView();
        populateRecyclerViewFromFirestore();

        return fragmentView;
    }

    private void setupRecyclerView() {
        workoutsRecyclerView.setHasFixedSize(true);
        workoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutsRecyclerView.setAdapter(new TrackingHistoryRecyclerViewAdapter(trackedWorkouts, getActivity()));
    }

    private void populateRecyclerViewFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference workoutsCollection = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("tracked_workouts");
        workoutsCollection.orderBy("dateOfWorkout", Query.Direction.DESCENDING).addSnapshotListener((documentSnapshots, e) -> {
            trackedWorkouts.clear();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                try {
                    TrackedWorkoutData queriedObject = documentSnapshot.toObject(TrackedWorkoutData.class).withId(documentSnapshot.getId());
                    if (queriedObject.validateObject()) {
                        trackedWorkouts.add(queriedObject);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            workoutsRecyclerView.getAdapter().notifyDataSetChanged();
            handleNoWorkoutTracked();
        });
    }

    private void handleNoWorkoutTracked() {
        if (trackedWorkouts.isEmpty()) {
            noWorkouts.setVisibility(View.VISIBLE);
            workoutsRecyclerView.setVisibility(View.GONE);
        } else {
            noWorkouts.setVisibility(View.GONE);
            workoutsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
