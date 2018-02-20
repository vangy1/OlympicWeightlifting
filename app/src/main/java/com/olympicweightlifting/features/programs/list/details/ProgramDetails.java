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
import com.olympicweightlifting.features.programs.list.ProgramsListRecyclerViewAdapter;
import com.olympicweightlifting.utilities.ApplicationConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_PROGRAMS;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_USERS;

public class ProgramDetails extends Fragment {
    public static final String TAG = ProgramDetails.class.getCanonicalName();

    @BindView(R.id.text_program_title)
    TextView textViewProgramTitle;
    @BindView(R.id.recyclerview_details)
    RecyclerView workoutsRecyclerView;
    @BindView(R.id.floatingbutton_program_remove)
    FloatingActionButton floatingButtonProgramRemove;

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

        program = new Gson().fromJson(getArguments().getString(ProgramsListRecyclerViewAdapter.BUNDLE_PROGRAM_DETAILS), new TypeToken<Program>() {
        }.getType());
        textViewProgramTitle.setText(program.getProgramTitle());
        setupRecyclerView();

        floatingButtonProgramRemove.setOnClickListener(view -> {
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
        CollectionReference programs = FirebaseFirestore.getInstance().collection(FIREBASE_COLLECTION_USERS).document(currentUser.getUid()).collection(FIREBASE_COLLECTION_PROGRAMS);
        programs.document(program.getDocumentId()).delete();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getFragmentManager().popBackStack();
        hideFragmentContainer();
    }

    private void hideFragmentContainer() {
        View workoutDetailsFragmentContainer = getActivity().findViewById(R.id.fragment_container_programs);
        workoutDetailsFragmentContainer.setVisibility(View.GONE);
    }
}
