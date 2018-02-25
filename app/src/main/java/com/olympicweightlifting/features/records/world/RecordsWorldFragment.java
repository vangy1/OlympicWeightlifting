package com.olympicweightlifting.features.records.world;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.records.world.data.WorldCategoryRecordsData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_RECORDS_WORLD;

public class RecordsWorldFragment extends Fragment {
    @BindView(R.id.recyclerview_records)
    RecyclerView recordsRecyclerView;

    private List<WorldCategoryRecordsData> worldCategoryRecordsDataList = new ArrayList<>();
    private ListenerRegistration worldRecordsRealtimeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_records_world, container, false);
        ButterKnife.bind(this, fragmentView);

        setupRecyclerView();
        populateRecyclerViewWithTheNewestDataFromFirestore();
        return fragmentView;
    }

    private void setupRecyclerView() {
        recordsRecyclerView.setHasFixedSize(true);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordsRecyclerView.setAdapter(new RecordsWorldRecyclerViewAdapter(worldCategoryRecordsDataList, getActivity()));
    }

    private void populateRecyclerViewWithTheNewestDataFromFirestore() {
        worldRecordsRealtimeListener = FirebaseFirestore.getInstance()
                .collection(FIREBASE_COLLECTION_RECORDS_WORLD)
                .orderBy("id")
                .addSnapshotListener((documentSnapshots, e) -> {
                    worldCategoryRecordsDataList.clear();
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        WorldCategoryRecordsData queriedObject = documentSnapshot.toObject(WorldCategoryRecordsData.class);

                        if (queriedObject.validateObject()) {
                            WorldCategoryRecordsData worldCategoryRecordsData = documentSnapshot.toObject(WorldCategoryRecordsData.class);
                            worldCategoryRecordsDataList.add(worldCategoryRecordsData);
                        }
                        recordsRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (worldRecordsRealtimeListener != null) worldRecordsRealtimeListener.remove();
        super.onDestroyView();
    }
}


