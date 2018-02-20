package com.olympicweightlifting.authentication;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.ProgramsInitialDataBuilder;
import com.olympicweightlifting.features.programs.data.Program;

import java.util.Arrays;
import java.util.List;


public class Authenticator {
    final int GOOGLE_LOGIN_REQUEST_CODE = 64205;
    final int FACEBOOK_LOGIN_REQUEST_CODE = 64206;

    private Activity activity;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private CallbackManager facebookCallbackManager;
    private AuthCredential facebookAuthCredential;

    private GoogleSignInClient googleSignInClient;


    public Authenticator(Activity activity) {
        this.activity = activity;

        setupGoogleAuthentication();
        setupFacebookAuthentication();
    }

    private void setupGoogleAuthentication() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.google_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);
    }

    private void setupFacebookAuthentication() {
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void firebaseAuthWithFacebook(AccessToken accessToken) {
        facebookAuthCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(facebookAuthCredential).addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            insertInitialDataToDatabase(currentUser);
                        }
                        ((AuthenticationActivity) activity).authenticationSuccess(currentUser);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(activity, R.string.signin_facebook_collision_message,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    private void insertInitialDataToDatabase(FirebaseUser currentUser) {
        CollectionReference programsCollection = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("programs");
        List<Program> initialPrograms = ProgramsInitialDataBuilder.getInitialPrograms();
        for (Program program : initialPrograms) {
            programsCollection.add(program);
        }

    }

    void checkIfAlreadySignedIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ((AuthenticationActivity) activity).alreadyAuthenticated();
        }
    }

    void startGoogleAuthentication() {
        activity.startActivityForResult(googleSignInClient.getSignInIntent(), GOOGLE_LOGIN_REQUEST_CODE);
    }

    void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            firebaseAuthWithGoogle(completedTask.getResult(ApiException.class));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (facebookAuthCredential != null) {
                    currentUser.linkWithCredential(facebookAuthCredential);
                }
                if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                    insertInitialDataToDatabase(currentUser);
                }
                ((AuthenticationActivity) activity).authenticationSuccess(currentUser);
            }
        });
    }

    void startFacebookAuthentication() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));
    }

    void handleFacebookSignInResult(int requestCode, int resultCode, Intent data) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
