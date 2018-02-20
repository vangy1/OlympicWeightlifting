package com.olympicweightlifting.authentication.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.olympicweightlifting.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.image_circle_user)
    CircleImageView imageCircleUser;
    @BindView(R.id.text_username)
    TextView textViewUsername;
    @BindView(R.id.text_user_account_status)
    TextView textViewAccountStatus;
    @BindView(R.id.button_signout)
    Button buttonSignout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setupToolbar();
        setFonts();

        setUserDetails(currentUser);
        buttonSignout.setOnClickListener(view -> signOut());

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewToolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_montserrat_bold)));
    }

    private void setFonts() {
        textViewUsername.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_montserrat_medium)));
        textViewAccountStatus.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_montserrat_bold)));
    }


    private void setUserDetails(FirebaseUser currentUser) {
        Picasso.with(this).load(currentUser.getPhotoUrl()).into(imageCircleUser);
        textViewUsername.setText(currentUser.getDisplayName());
        textViewAccountStatus.setText("Free");
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

}
