package com.olympicweightlifting.authentication.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.billingclient.api.Purchase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.olympicweightlifting.R;
import com.olympicweightlifting.billing.BillingListener;
import com.olympicweightlifting.billing.BillingManager;
import com.olympicweightlifting.features.programs.ProgramsInitialDataBuilder;
import com.olympicweightlifting.features.programs.data.Program;
import com.olympicweightlifting.features.records.personal.RecordsPersonalData;
import com.olympicweightlifting.features.tracking.data.TrackedWorkoutData;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_PROGRAMS;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_RECORDS_PERSONAL;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_USERS;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_WORKOUTS;
import static com.olympicweightlifting.utilities.ApplicationConstants.PROGRAMS_LIMIT;
import static com.olympicweightlifting.utilities.ApplicationConstants.RECORDS_LIMIT;
import static com.olympicweightlifting.utilities.ApplicationConstants.WORKOUTS_LIMIT;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.image_circle_user)
    CircleImageView imageCircleUser;
    @BindView(R.id.text_username)
    TextView textViewUsername;
    @BindView(R.id.text_premium_purchased_message)
    TextView textViewPremiumPurchasedMessage;

    @BindView(R.id.text_programs_limit)
    TextView textViewProgramsLimit;
    @BindView(R.id.text_workouts_plan_limit)
    TextView textViewWorkoutsLimit;
    @BindView(R.id.text_records_personal_limit)
    TextView textViewRecordsLimit;

    @BindView(R.id.button_signout)
    Button buttonSignout;
    @BindView(R.id.button_upgrade_premium)
    Button buttonUpgradePremium;
    @BindView(R.id.layout_limits)
    ViewGroup layoutLimits;

    BillingManager billingManager;
    private ListenerRegistration programsRealtimeListener;
    private ListenerRegistration workoutsRealtimeListener;
    private ListenerRegistration recordsRealtimeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getUserProfileStatus();

        setupToolbar();
        setFonts();
        setUserDetails();
        buttonSignout.setOnClickListener(view -> signOut());
    }


    private void getUserProfileStatus() {
        billingManager = new BillingManager(this, new BillingListener() {

            @Override
            public void onPurchasesQueried(List<Purchase> purchases) {
                for (Purchase purchase : purchases) {
                    purchase.getSku();
                }
                if (billingManager.isUserPremium(purchases)) {
                    handlePremiumUser(purchases);
                } else {
                    handleFreeUser();
                }
            }

            @Override
            public void onPurchasesUpdated(List<Purchase> purchases) {
                if (billingManager.isUserPremium(purchases)) {
                    List<Program> initialPrograms = ProgramsInitialDataBuilder.getInitialPrograms();
                    for (Program program : initialPrograms) {
                        program.setDateAdded();
                        FirebaseFirestore.getInstance()
                                .collection(FIREBASE_COLLECTION_USERS)
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection(FIREBASE_COLLECTION_PROGRAMS).add(program);
                    }
                    onPurchasesQueried(purchases);
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewToolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_montserrat_bold)));
    }

    private void setFonts() {
        textViewUsername.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_montserrat_medium)));
    }

    private void setUserDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Picasso.with(this).load(currentUser.getPhotoUrl()).into(imageCircleUser);
        textViewUsername.setText(currentUser.getDisplayName());
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    private void handlePremiumUser(List<Purchase> purchases) {
        layoutLimits.setVisibility(View.GONE);
        textViewPremiumPurchasedMessage.setVisibility(View.VISIBLE);
    }

    private void handleFreeUser() {
        layoutLimits.setVisibility(View.VISIBLE);
        textViewPremiumPurchasedMessage.setVisibility(View.GONE);
        buttonUpgradePremium.setOnClickListener(view -> billingManager.initiatePurchaseFlow());
        setListenersToGetLimitsInfo();
    }

    private void setListenersToGetLimitsInfo() {
        programsRealtimeListener = FirebaseFirestore.getInstance()
                .collection(FIREBASE_COLLECTION_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection(FIREBASE_COLLECTION_PROGRAMS)
                .addSnapshotListener((documentSnapshots, e) -> {
                    int validPrograms = 0;
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.toObject(Program.class).validateObject()) {
                            validPrograms += 1;
                        }
                    }
                    textViewProgramsLimit.setText(String.format(getString(R.string.profile_programs_created_info) + " %d/%d", validPrograms, PROGRAMS_LIMIT));
                });
        workoutsRealtimeListener = FirebaseFirestore.getInstance()
                .collection(FIREBASE_COLLECTION_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection(FIREBASE_COLLECTION_WORKOUTS)
                .addSnapshotListener((documentSnapshots, e) -> {
                    int validWorkouts = 0;
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.toObject(TrackedWorkoutData.class).validateObject()) {
                            validWorkouts += 1;
                        }
                    }
                    textViewWorkoutsLimit.setText(String.format(getString(R.string.profile_workouts_tracked_info) + " %d/%d", validWorkouts, WORKOUTS_LIMIT));
                });
        recordsRealtimeListener = FirebaseFirestore.getInstance()
                .collection(FIREBASE_COLLECTION_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection(FIREBASE_COLLECTION_RECORDS_PERSONAL)
                .addSnapshotListener((documentSnapshots, e) -> {
                    int validRecords = 0;
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.toObject(RecordsPersonalData.class).validateObject()) {
                            validRecords += 1;
                        }
                    }
                    textViewRecordsLimit.setText(String.format(getString(R.string.profile_records_tracked_info) + " %d/%d", validRecords, RECORDS_LIMIT));
                });
    }

    @Override
    protected void onDestroy() {
        if (billingManager != null) billingManager.destroy();
        if (programsRealtimeListener != null) programsRealtimeListener.remove();
        if (workoutsRealtimeListener != null) workoutsRealtimeListener.remove();
        if (recordsRealtimeListener != null) recordsRealtimeListener.remove();
        super.onDestroy();
    }
}
