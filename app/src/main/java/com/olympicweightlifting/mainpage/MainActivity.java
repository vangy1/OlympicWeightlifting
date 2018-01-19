package com.olympicweightlifting.mainpage;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.olympicweightlifting.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.features_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;

    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();
        setupRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Typeface samsungSansBold = Typeface.createFromAsset(getAssets(), "SamsungSans-Bold.ttf");
        toolbarTitle.setTypeface(samsungSansBold);
    }

    private void setupRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        setupAndPopulateRecyclerAdapter();
    }

    private void setupAndPopulateRecyclerAdapter() {
        Resources resources = getResources();

        FeatureDataset snatchDataset = new FeatureDataset(resources.getString(R.string.snatch), resources.getStringArray(R.array.snatch_shortcuts), R.drawable.feature_image_snatch);
        FeatureDataset cajDataset = new FeatureDataset(resources.getString(R.string.caj), resources.getStringArray(R.array.caj_shortcuts), R.drawable.feature_image_caj);
        FeatureDataset calculatorsDataset = new FeatureDataset(resources.getString(R.string.calculators), resources.getStringArray(R.array.calculators_shortcuts), R.drawable.feature_image_calculators);
        FeatureDataset programsDataset = new FeatureDataset(resources.getString(R.string.programs), resources.getStringArray(R.array.programs_shortcuts), R.drawable.feature_image_programs);
        FeatureDataset trackingDataset = new FeatureDataset(resources.getString(R.string.tracking), resources.getStringArray(R.array.tracking_shortcuts), R.drawable.feature_image_tracking);
        FeatureDataset recordsDataset = new FeatureDataset(resources.getString(R.string.records), resources.getStringArray(R.array.records_shortcuts), R.drawable.feature_image_records);
        recyclerViewAdapter = new FeatureCardsAdapter(new FeatureDataset[]{snatchDataset, cajDataset, calculatorsDataset, programsDataset, trackingDataset, recordsDataset},this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
