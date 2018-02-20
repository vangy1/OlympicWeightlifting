package com.olympicweightlifting.mainpage;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.olympicweightlifting.R;
import com.olympicweightlifting.authentication.AuthenticationActivity;
import com.olympicweightlifting.authentication.SignInDialog;
import com.olympicweightlifting.authentication.profile.ProfileActivity;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.calculators.CalculatorsActivity;
import com.olympicweightlifting.features.lifts.LiftsActivity;
import com.olympicweightlifting.features.lifts.LiftsContentDataBuilder;
import com.olympicweightlifting.features.programs.ProgramsActivity;
import com.olympicweightlifting.features.records.RecordsActivity;
import com.olympicweightlifting.features.tracking.TrackingActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements AuthenticationActivity {
    public static final String BUNDLE_LIFTS_ACTIVITY_DATA = "LIFTS_ACTIVITY_DATA";
    public static final String BUNDLE_LIFTS_HEADER_IMAGE = "BUNDLE_LIFTS_HEADER_IMAGE";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.features_recycler_view)
    RecyclerView recyclerViewFeatures;

    SignInDialog signInDialog;

    @Inject
    AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // to make sure that database object will be constructed before next activities
        database.getOpenHelper().getWritableDatabase();

        setupToolbar();
        setupRecyclerView();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textViewToolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_samsung_sans_bold)));
    }

    private void setupRecyclerView() {
        recyclerViewFeatures.setHasFixedSize(true);
        recyclerViewFeatures.setLayoutManager(new LinearLayoutManager(this));

        setupAndPopulateRecyclerAdapter();
    }

    private void setupAndPopulateRecyclerAdapter() {
        Resources resources = getResources();
        LiftsContentDataBuilder liftsContentDataBuilder = new LiftsContentDataBuilder(getApplicationContext());

        Bundle snatchActivityBundle = getLiftsBundle(new Gson().toJson(liftsContentDataBuilder.getContentDataSnatch()), R.drawable.lifts_image_snatch);
        FeatureDataset snatchDataset = new FeatureDataset(resources.getString(R.string.all_snatch), resources.getStringArray(R.array.snatch_shortcuts), R.drawable.feature_image_snatch, LiftsActivity.class, snatchActivityBundle);

        Bundle cajActivityBundle = getLiftsBundle(new Gson().toJson(liftsContentDataBuilder.getContentDataCaj()), R.drawable.lifts_image_caj);
        FeatureDataset cajDataset = new FeatureDataset(resources.getString(R.string.all_caj), resources.getStringArray(R.array.caj_shortcuts), R.drawable.feature_image_caj, LiftsActivity.class, cajActivityBundle);

        FeatureDataset calculatorsDataset = new FeatureDataset(resources.getString(R.string.all_calculators), resources.getStringArray(R.array.calculators_shortcuts), R.drawable.feature_image_calculators, CalculatorsActivity.class);
        FeatureDataset programsDataset = new FeatureDataset(resources.getString(R.string.all_programs), resources.getStringArray(R.array.programs_shortcuts), R.drawable.feature_image_programs, ProgramsActivity.class);
        FeatureDataset trackingDataset = new FeatureDataset(resources.getString(R.string.all_tracking), resources.getStringArray(R.array.tracking_shortcuts), R.drawable.feature_image_tracking, TrackingActivity.class);
        FeatureDataset recordsDataset = new FeatureDataset(resources.getString(R.string.all_records), resources.getStringArray(R.array.records_shortcuts), R.drawable.feature_image_records, RecordsActivity.class);

        RecyclerView.Adapter recyclerViewAdapter = new FeaturesRecyclerViewAdapter(new FeatureDataset[]{snatchDataset, cajDataset, calculatorsDataset, programsDataset, trackingDataset, recordsDataset}, this);
        recyclerViewFeatures.setAdapter(recyclerViewAdapter);
    }

    private Bundle getLiftsBundle(String activityDataSerialized, int activityHeaderImage) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_LIFTS_HEADER_IMAGE, activityHeaderImage);
        bundle.putString(BUNDLE_LIFTS_ACTIVITY_DATA, activityDataSerialized);
        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            menu.findItem(R.id.profile).setVisible(false);
            menu.findItem(R.id.signin).setVisible(true);
        } else {
            menu.findItem(R.id.signin).setVisible(false);
            menu.findItem(R.id.profile).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            showSettingsDialog();
        } else if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (item.getItemId() == R.id.signin) {
            signInDialog = new SignInDialog();
            signInDialog.show(getSupportFragmentManager(), SignInDialog.TAG);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingsDialog() {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getSupportFragmentManager(), SettingsDialog.TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        signInDialog.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void alreadyAuthenticated() {
        signInDialog.dismiss();
    }

    @Override
    public void authenticationSuccess(FirebaseUser user) {
        signInDialog.dismiss();
        Toast.makeText(this, getString(R.string.signin_profile_identification) + user.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}
