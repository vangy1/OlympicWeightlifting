package com.olympicweightlifting.authentication;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.olympicweightlifting.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInFragment extends Fragment {

    @BindView(R.id.button_signin)
    Button buttonSignin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, fragmentView);

        buttonSignin.setOnClickListener(view -> startSignInActivity());

        return fragmentView;
    }

    private void startSignInActivity() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
