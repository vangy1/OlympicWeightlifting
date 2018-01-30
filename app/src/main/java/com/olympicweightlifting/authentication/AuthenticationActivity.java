package com.olympicweightlifting.authentication;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by vangor on 28/01/2018.
 */

public interface AuthenticationActivity {
    void alreadyAuthenticated();
    void authenticationSuccess(FirebaseUser user);
}
