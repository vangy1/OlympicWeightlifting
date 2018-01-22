package com.olympicweightlifting.mainpage;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.calculators.CalculatorsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.features_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            showSettingsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_samsung_sans_bold)));
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupAndPopulateRecyclerAdapter();
    }

    private void setupAndPopulateRecyclerAdapter() {
        Resources resources = getResources();

        FeatureDataset snatchDataset = new FeatureDataset(resources.getString(R.string.snatch), resources.getStringArray(R.array.snatch_shortcuts), R.drawable.feature_image_snatch, new Intent(this, CalculatorsActivity.class));
        FeatureDataset cajDataset = new FeatureDataset(resources.getString(R.string.caj), resources.getStringArray(R.array.caj_shortcuts), R.drawable.feature_image_caj, new Intent(this, CalculatorsActivity.class));
        FeatureDataset calculatorsDataset = new FeatureDataset(resources.getString(R.string.calculators), resources.getStringArray(R.array.calculators_shortcuts), R.drawable.feature_image_calculators, new Intent(this, CalculatorsActivity.class));
        FeatureDataset programsDataset = new FeatureDataset(resources.getString(R.string.programs), resources.getStringArray(R.array.programs_shortcuts), R.drawable.feature_image_programs, new Intent(this, CalculatorsActivity.class));
        FeatureDataset trackingDataset = new FeatureDataset(resources.getString(R.string.tracking), resources.getStringArray(R.array.tracking_shortcuts), R.drawable.feature_image_tracking, new Intent(this, CalculatorsActivity.class));
        FeatureDataset recordsDataset = new FeatureDataset(resources.getString(R.string.records), resources.getStringArray(R.array.records_shortcuts), R.drawable.feature_image_records, new Intent(this, CalculatorsActivity.class));

        RecyclerView.Adapter recyclerViewAdapter = new FeatureCardsRecyclerViewAdapter(new FeatureDataset[]{snatchDataset, cajDataset, calculatorsDataset, programsDataset, trackingDataset, recordsDataset}, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void showSettingsDialog() {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getFragmentManager(), getString(R.string.settings_dialog_fragment_tag));
    }
}
