package com.olympicweightlifting.features.records.world;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.olympicweightlifting.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordsWorldFragment extends Fragment {
    @BindView(R.id.records_recycler_view)
    RecyclerView recordsRecyclerView;

    private List<WorldCategoryRecordsData> worldCategoryRecordsDataList = new ArrayList<>();

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
        CollectionReference worldRecords = FirebaseFirestore.getInstance().collection(getString(R.string.firestore_collection_world_records));
        worldRecords.orderBy(getString(R.string.firestore_collection_world_records_id_column)).addSnapshotListener((documentSnapshots, e) -> {
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

}


