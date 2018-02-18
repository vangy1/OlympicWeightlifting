package com.olympicweightlifting.authentication;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.olympicweightlifting.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInDialog extends DialogFragment {

    @BindView(R.id.dialog_title)
    TextView dialogTitle;
    @BindView(R.id.google_signin_button)
    Button googleSigninButton;
    @BindView(R.id.facebook_signin_button)
    Button facebookSigninButton;

    private Authenticator authenticator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_sign_in, null);
        ButterKnife.bind(this, dialogView);
        dialogTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold)));
        authenticator = new Authenticator(getActivity());

        googleSigninButton.setOnClickListener(view -> authenticator.startGoogleAuthentication());
        facebookSigninButton.setOnClickListener(view -> authenticator.startFacebookAuthentication());

        return new AlertDialog.Builder(getActivity()).setView(dialogView).create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == authenticator.GOOGLE_LOGIN_REQUEST_CODE) {
            authenticator.handleGoogleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        } else if (requestCode == authenticator.FACEBOOK_LOGIN_REQUEST_CODE) {
            authenticator.handleFacebookSignInResult(requestCode, resultCode, data);
        }
    }
}
