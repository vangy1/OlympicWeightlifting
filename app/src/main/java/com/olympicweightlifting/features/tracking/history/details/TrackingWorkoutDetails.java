package com.olympicweightlifting.features.tracking.history.details;


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
import com.olympicweightlifting.features.tracking.data.TrackedWorkoutData;
import com.olympicweightlifting.features.tracking.history.TrackingHistoryRecyclerViewAdapter;
import com.olympicweightlifting.utilities.ApplicationConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateFormat.getDateFormat;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_USERS;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_WORKOUTS_TRACKED;

public class TrackingWorkoutDetails extends Fragment {
    public static final String TAG = TrackingWorkoutDetails.class.getCanonicalName();

    @BindView(R.id.text_workout_date)
    TextView textViewWorkoutDate;
    @BindView(R.id.recyclerview_workouts)
    RecyclerView recyclerViewWorkouts;
    @BindView(R.id.floatingbutton_remove_workout)
    FloatingActionButton floatingButtonRemoveWorkout;

    private TrackedWorkoutData trackedWorkout;

    public static TrackingWorkoutDetails newInstance(Bundle fragmentArugments) {
        TrackingWorkoutDetails trackingWorkoutDetails = new TrackingWorkoutDetails();
        trackingWorkoutDetails.setArguments(fragmentArugments);
        return trackingWorkoutDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_tracking_workout_details, container, false);
        ButterKnife.bind(this, fragmentView);

        trackedWorkout = new Gson().fromJson(getArguments().getString(TrackingHistoryRecyclerViewAdapter.BUNDLE_WORKOUT_DETAILS), new TypeToken<TrackedWorkoutData>() {
        }.getType());
        textViewWorkoutDate.setText(getDateFormat(getActivity()).format(trackedWorkout.getDateOfWorkout()));
        setupRecyclerView();

        floatingButtonRemoveWorkout.setOnClickListener(view -> {
            removeWorkoutFromFirestore();
            getActivity().getFragmentManager().beginTransaction()
                    .remove(TrackingWorkoutDetails.this)
                    .commit();

        });

        return fragmentView;
    }

    private void setupRecyclerView() {
        recyclerViewWorkouts.setHasFixedSize(true);
        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewWorkouts.setAdapter(new TrackingWorkoutDetailsRecyclerViewAdapter(trackedWorkout.getTrackedExercises(), getActivity()));
    }

    private void removeWorkoutFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference trackedWorkouts = FirebaseFirestore.getInstance().collection(FIREBASE_COLLECTION_USERS).document(currentUser.getUid()).collection(FIREBASE_COLLECTION_WORKOUTS_TRACKED);
        trackedWorkouts.document(trackedWorkout.getDocumentId()).delete();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getFragmentManager().popBackStack();
        hideFragmentContainer();
    }

    private void hideFragmentContainer() {
        View workoutDetailsFragmentContainer = getActivity().findViewById(R.id.fragment_container_workouts);
        workoutDetailsFragmentContainer.setVisibility(View.GONE);
    }
}
