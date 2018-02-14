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
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.user_image)
    CircleImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_account_status)
    TextView userAccountStatus;
    @BindView(R.id.signout_button)
    Button signoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setupToolbar();
        setFonts();

        setUserDetails(currentUser);
        signoutButton.setOnClickListener(view -> signOut());

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_montserrat_bold)));
    }

    private void setFonts() {
        userName.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_montserrat_medium)));
        userAccountStatus.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_path_montserrat_bold)));
    }


    private void setUserDetails(FirebaseUser currentUser) {
        Picasso.with(this).load(currentUser.getPhotoUrl()).into(userImage);
        userName.setText(currentUser.getDisplayName());
        userAccountStatus.setText("Free");
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

}
