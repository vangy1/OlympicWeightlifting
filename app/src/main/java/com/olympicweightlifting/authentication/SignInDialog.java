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
    public static final String TAG = SignInDialog.class.getCanonicalName();

    @BindView(R.id.text_dialog_title)
    TextView textViewDialogTitle;
    @BindView(R.id.button_google_signin)
    Button buttonGoogleSignin;
    @BindView(R.id.button_facebook_signin)
    Button buttonFacebookSignin;

    private Authenticator authenticator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View viewDialog = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_signin, null);
        ButterKnife.bind(this, viewDialog);
        textViewDialogTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold)));
        authenticator = new Authenticator(getActivity());

        buttonGoogleSignin.setOnClickListener(view -> authenticator.startGoogleAuthentication());
        buttonFacebookSignin.setOnClickListener(view -> authenticator.startFacebookAuthentication());

        return new AlertDialog.Builder(getActivity()).setView(viewDialog).create();
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
