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
import com.olympicweightlifting.features.lifts.LiftsContentDataUtility;
import com.olympicweightlifting.features.records.RecordsActivity;
import com.olympicweightlifting.features.tracking.TrackingActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements AuthenticationActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.features_recycler_view)
    RecyclerView featuresRecyclerView;
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
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_samsung_sans_bold)));
    }

    private void setupRecyclerView() {
        featuresRecyclerView.setHasFixedSize(true);
        featuresRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupAndPopulateRecyclerAdapter();
    }

    private void setupAndPopulateRecyclerAdapter() {
        Resources resources = getResources();
        LiftsContentDataUtility liftsContentDataUtility = new LiftsContentDataUtility(getApplicationContext());

        Bundle snatchActivityBundle = getLiftsBundle(new Gson().toJson(liftsContentDataUtility.getContentDataSnatch()), R.drawable.lifts_image_snatch);
        FeatureDataset snatchDataset = new FeatureDataset(resources.getString(R.string.snatch), resources.getStringArray(R.array.snatch_shortcuts), R.drawable.feature_image_snatch, LiftsActivity.class, snatchActivityBundle);

        Bundle cajActivityBundle = getLiftsBundle(new Gson().toJson(liftsContentDataUtility.getContentDataCaj()), R.drawable.lifts_image_caj);
        FeatureDataset cajDataset = new FeatureDataset(resources.getString(R.string.caj), resources.getStringArray(R.array.caj_shortcuts), R.drawable.feature_image_caj, LiftsActivity.class, cajActivityBundle);

        FeatureDataset calculatorsDataset = new FeatureDataset(resources.getString(R.string.calculators), resources.getStringArray(R.array.calculators_shortcuts), R.drawable.feature_image_calculators, CalculatorsActivity.class);
        FeatureDataset programsDataset = new FeatureDataset(resources.getString(R.string.programs), resources.getStringArray(R.array.programs_shortcuts), R.drawable.feature_image_programs, CalculatorsActivity.class);
        FeatureDataset trackingDataset = new FeatureDataset(resources.getString(R.string.tracking), resources.getStringArray(R.array.tracking_shortcuts), R.drawable.feature_image_tracking, TrackingActivity.class);
        FeatureDataset recordsDataset = new FeatureDataset(resources.getString(R.string.records), resources.getStringArray(R.array.records_shortcuts), R.drawable.feature_image_records, RecordsActivity.class);

        RecyclerView.Adapter recyclerViewAdapter = new FeatureCardsRecyclerViewAdapter(new FeatureDataset[]{snatchDataset, cajDataset, calculatorsDataset, programsDataset, trackingDataset, recordsDataset}, this);
        featuresRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private Bundle getLiftsBundle(String activityDataSerialized, int activityHeaderImage) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.lifts_header_image), activityHeaderImage);
        bundle.putString(getString(R.string.lifts_activity_data), activityDataSerialized);
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
            signInDialog.show(getFragmentManager(), getString(R.string.signin_dialog_fragment_tag));
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingsDialog() {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getFragmentManager(), getString(R.string.settings_dialog_fragment_tag));
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
        Toast.makeText(this, "Signed in as " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}
