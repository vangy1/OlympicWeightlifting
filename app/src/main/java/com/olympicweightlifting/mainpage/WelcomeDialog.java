package com.olympicweightlifting.mainpage;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.olympicweightlifting.App;
import com.olympicweightlifting.R;
import com.olympicweightlifting.authentication.profile.ProfileActivity;
import com.olympicweightlifting.utilities.ApplicationConstants;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatDialogFragment;


public class WelcomeDialog extends DaggerAppCompatDialogFragment {
    public static final String TAG = Settings.class.getCanonicalName();

    @Inject
    @Named("app-info")
    SharedPreferences appInfoSharedPreferences;

    @BindView(R.id.text_dialog_title)
    TextView dialogTitle;
    @BindView(R.id.dialog_message)
    TextView dialogMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_welcome, null);
        ButterKnife.bind(this, dialogView);
        ((App) getActivity().getApplication()).getAnalyticsTracker().sendEvent("Welcome Dialog", "Prompt", "Premium");

        dialogTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold)));
        dialogMessage.setText(R.string.welcome_message);

        return new AlertDialog.Builder(getActivity()).setView(dialogView)
                .setNegativeButton(R.string.all_cancel, (dialog, i) -> {
                    dialog.dismiss();
                    ((App) getActivity().getApplication()).getAnalyticsTracker().sendEvent("Welcome Dialog", "Prompt - Cancel click", "Premium");
                })
                .setPositiveButton(R.string.all_upgrade, (dialog, id) -> {
                    getActivity().startActivity(new Intent(getActivity(), ProfileActivity.class));
                    ((App) getActivity().getApplication()).getAnalyticsTracker().sendEvent("Welcome Dialog", "Prompt - Upgrade click", "Premium");
                }).create();
    }

    @Override
    public void onDestroyView() {
        setIsFirstRunToFalse();
        super.onDestroyView();
    }

    private void setIsFirstRunToFalse() {
        appInfoSharedPreferences.edit().putBoolean(ApplicationConstants.PREF_APP_INFO_IS_FIRST_RUN, false).apply();
    }
}
