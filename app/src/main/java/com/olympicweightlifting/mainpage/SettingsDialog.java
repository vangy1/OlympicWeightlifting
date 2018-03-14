package com.olympicweightlifting.mainpage;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.olympicweightlifting.R;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatDialogFragment;

import static com.olympicweightlifting.utilities.ApplicationConstants.PREF_SETTINGS_UNITS;
import static com.olympicweightlifting.utilities.ApplicationConstants.Units;


public class SettingsDialog extends DaggerAppCompatDialogFragment {
    public static final String TAG = Settings.class.getCanonicalName();

    @BindView(R.id.text_dialog_title)
    TextView dialogTitle;
    @BindView(R.id.switch_dark_theme)
    Switch switchDarkTheme;
    @BindView(R.id.radiogroup_units)
    RadioGroup unitsRadioGroup;
    @BindView(R.id.button_settings_save)
    Button buttonSettingsSave;

    @Inject
    @Named("settings")
    SharedPreferences settingsSharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_settings, null);
        ButterKnife.bind(this, dialogView);
        dialogTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold)));

        displayCurrentSettings();
        buttonSettingsSave.setOnClickListener(view -> {
            saveSettingsToSharedPreferences();
            this.dismiss();
        });

        return new AlertDialog.Builder(getActivity()).setView(dialogView).create();
    }

    private void displayCurrentSettings() {
        switchDarkTheme.setChecked(settingsSharedPreferences.getBoolean(getString(R.string.all_dark_theme), false));
        Units units = Units.valueOf(settingsSharedPreferences.getString(PREF_SETTINGS_UNITS, Units.KG.toString()));
        if (units == Units.KG) {
            unitsRadioGroup.check(R.id.kg_radio_button);
        } else if (units == Units.LB) {
            unitsRadioGroup.check(R.id.lb_radio_button);
        }
    }

    private void saveSettingsToSharedPreferences() {
        SharedPreferences.Editor sharedPreferencesEditor = settingsSharedPreferences.edit();
        if (settingsSharedPreferences.getBoolean(getString(R.string.all_dark_theme), false) != switchDarkTheme.isChecked()) {
            sharedPreferencesEditor.putBoolean(getString(R.string.all_dark_theme), switchDarkTheme.isChecked());
            getActivity().recreate();
        }
        if (unitsRadioGroup.getCheckedRadioButtonId() == R.id.kg_radio_button) {
            sharedPreferencesEditor.putString(PREF_SETTINGS_UNITS, Units.KG.toString());
        } else if (unitsRadioGroup.getCheckedRadioButtonId() == R.id.lb_radio_button) {
            sharedPreferencesEditor.putString(PREF_SETTINGS_UNITS, Units.LB.toString());
        }
        sharedPreferencesEditor.apply();
    }

}
