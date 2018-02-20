package com.olympicweightlifting.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseUser;
import com.olympicweightlifting.R;
import com.olympicweightlifting.mainpage.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements AuthenticationActivity {
    @BindView(R.id.button_google_signin)
    Button buttonGoogleSignin;
    @BindView(R.id.button_facebook_signin)
    Button buttonFacebookSignin;
    @BindView(R.id.button_guest)
    Button buttonGuest;

    private Authenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
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
