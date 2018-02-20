package com.olympicweightlifting.features.tracking.history;


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
import com.olympicweightlifting.features.tracking.data.TrackedWorkoutData;
import com.olympicweightlifting.utilities.ApplicationConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_USERS;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_WORKOUTS_TRACKED;


public class TrackingHistoryFragment extends Fragment {


    @BindView(R.id.recyclerview_workouts)
    RecyclerView recyclerViewWorkouts;
    @BindView(R.id.text_no_workouts_saved)
    TextView textViewNoWorkoutsSaved;

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
        recyclerViewWorkouts.setHasFixedSize(true);
        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewWorkouts.setAdapter(new TrackingHistoryRecyclerViewAdapter(trackedWorkouts, getActivity()));
    }

    private void populateRecyclerViewFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference workoutsCollection = FirebaseFirestore.getInstance().collection(FIREBASE_COLLECTION_USERS).document(currentUser.getUid()).collection(FIREBASE_COLLECTION_WORKOUTS_TRACKED);
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
            recyclerViewWorkouts.getAdapter().notifyDataSetChanged();
            handleNoWorkoutTracked();
        });
    }

    private void handleNoWorkoutTracked() {
        if (trackedWorkouts.isEmpty()) {
            textViewNoWorkoutsSaved.setVisibility(View.VISIBLE);
            recyclerViewWorkouts.setVisibility(View.GONE);
        } else {
            textViewNoWorkoutsSaved.setVisibility(View.GONE);
            recyclerViewWorkouts.setVisibility(View.VISIBLE);
        }
    }

}
