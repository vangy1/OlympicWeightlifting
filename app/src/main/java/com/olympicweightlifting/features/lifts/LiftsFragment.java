package com.olympicweightlifting.features.lifts;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olympicweightlifting.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiftsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.youtube_floating_button)
    FloatingActionButton floatingActionButton;

    public static LiftsFragment newInstance(Bundle fragmentArugments) {
        LiftsFragment liftsFragment = new LiftsFragment();
        liftsFragment.setArguments(fragmentArugments);
        return liftsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_lifts, container, false);
        ButterKnife.bind(this, fragmentView);

        setupRecyclerView(new Gson().fromJson(getArguments().getString(getString(R.string.lifts_content_data_list)), new TypeToken<List<LiftsContentData>>() {
        }.getType()));
        setupFloatingButton((ViewGroup) fragmentView);

        return fragmentView;
    }

    private void setupRecyclerView(ArrayList<LiftsContentData> liftsContentDataList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new LiftsFragmentRecyclerViewAdapter(liftsContentDataList));
    }

    private void setupFloatingButton(ViewGroup fragmentView) {
        String videoUrl = getArguments().getString(getString(R.string.floating_button_video_url));
        if (videoUrl == null) {
            fragmentView.removeView(floatingActionButton);
        } else {
            floatingActionButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                startActivity(intent);
            });
        }
    }
}
