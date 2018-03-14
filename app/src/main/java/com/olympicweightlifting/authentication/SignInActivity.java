package com.olympicweightlifting.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseUser;
import com.olympicweightlifting.App;
import com.olympicweightlifting.R;
import com.olympicweightlifting.mainpage.MainActivity;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class SignInActivity extends DaggerAppCompatActivity implements AuthenticationActivity {
    @BindView(R.id.button_google_signin)
    Button buttonGoogleSignin;
    @BindView(R.id.button_facebook_signin)
    Button buttonFacebookSignin;
    @BindView(R.id.button_guest)
    Button buttonGuest;
    @Inject
    @Named("settings")
    SharedPreferences settingsSharedPreferences;
    private Authenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (settingsSharedPreferences.getBoolean(getString(R.string.all_dark_theme), false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        ((App) getApplication()).getAnalyticsTracker().sendScreenName("SignIn Activity");

        authenticator = new Authenticator(this);

        authenticator.checkIfAlreadySignedIn();
        buttonGuest.setOnClickListener(view -> moveToMainActivity());
        buttonGoogleSignin.setOnClickListener(view -> authenticator.startGoogleAuthentication());
        buttonFacebookSignin.setOnClickListener(view -> authenticator.startFacebookAuthentication());
    }

    private void moveToMainActivity() {
        this.startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == authenticator.GOOGLE_LOGIN_REQUEST_CODE) {
            authenticator.handleGoogleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        } else if (requestCode == authenticator.FACEBOOK_LOGIN_REQUEST_CODE) {
            authenticator.handleFacebookSignInResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void alreadyAuthenticated() {
        moveToMainActivity();
    }

    @Override
    public void authenticationSuccess(FirebaseUser user) {
        moveToMainActivity();
        Toast.makeText(this, getString(R.string.signin_profile_identification) + user.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}
