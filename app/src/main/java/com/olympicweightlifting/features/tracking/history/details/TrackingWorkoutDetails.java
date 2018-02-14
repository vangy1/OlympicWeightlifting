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
import com.olympicweightlifting.features.tracking.TrackedWorkoutData;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateFormat.getDateFormat;

public class TrackingWorkoutDetails extends Fragment {
    @BindView(R.id.workout_date)
    TextView workoutDate;
    @BindView(R.id.workouts_recycler_view)
    RecyclerView workoutsRecyclerView;
    @BindView(R.id.delete_floating_button)
    FloatingActionButton deleteWorkoutFloatingButton;
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

        trackedWorkout = new Gson().fromJson(getArguments().getString("workoutDetails"), new TypeToken<TrackedWorkoutData>() {
        }.getType());
        workoutDate.setText(getDateFormat(getActivity()).format(trackedWorkout.getDateOfWorkout()));
        setupRecyclerView();

        deleteWorkoutFloatingButton.setOnClickListener(view -> {
            removeWorkoutFromFirestore();
            getActivity().getFragmentManager().beginTransaction()
                    .remove(TrackingWorkoutDetails.this)
                    .commit();

        });

        return fragmentView;
    }

    private void setupRecyclerView() {
        workoutsRecyclerView.setHasFixedSize(true);
        workoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutsRecyclerView.setAdapter(new TrackingWorkoutDetailsRecyclerViewAdapter(trackedWorkout.getTrackedExercises(), getActivity()));
    }

    private void removeWorkoutFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference trackedWorkouts = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("tracked_workouts");
        trackedWorkouts.document(trackedWorkout.getDocumentId()).delete();
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
