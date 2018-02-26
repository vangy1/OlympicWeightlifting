package com.olympicweightlifting.authentication;

import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationActivity {
    void alreadyAuthenticated();

    void authenticationSuccess(FirebaseUser user);
}
