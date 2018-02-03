package com.olympicweightlifting.mainpage;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.olympicweightlifting.R;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerDialogFragment;

public class SettingsDialog extends DaggerDialogFragment {
    @BindView(R.id.dialog_title)
    TextView dialogTitle;
    @BindView(R.id.dark_theme_switch)
    Switch darkThemeSwitch;
    @BindView(R.id.units_radio_group)
    RadioGroup unitsRadioGroup;

    @Inject
    @Named("settings")
    SharedPreferences settingsSharedPreferences;

    public enum Units {
        KG, LB
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_settings, null);
        ButterKnife.bind(this, dialogView);
        dialogTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold)));

        displayCurrentSettings();

        return new AlertDialog.Builder(getActivity()).setView(dialogView)
                .setPositiveButton(R.string.settings_positive_button_text, (dialog, id) -> saveSettingsToSharedPreferences()).create();
    }

    private void displayCurrentSettings() {
        darkThemeSwitch.setChecked(settingsSharedPreferences.getBoolean(getString(R.string.settings_dark_theme), false));
        Units units = Units.valueOf(settingsSharedPreferences.getString(getString(R.string.settings_units), Units.KG.toString()));
        if (units == Units.KG) {
            unitsRadioGroup.check(R.id.kg_radio_button);
        } else if (units == Units.LB) {
            unitsRadioGroup.check(R.id.lb_radio_button);
        }
    }

    private void saveSettingsToSharedPreferences() {
        SharedPreferences.Editor sharedPreferencesEditor = settingsSharedPreferences.edit();
        sharedPreferencesEditor.putBoolean(getString(R.string.settings_dark_theme), darkThemeSwitch.isChecked());
        if (unitsRadioGroup.getCheckedRadioButtonId() == R.id.kg_radio_button) {
            sharedPreferencesEditor.putString(getString(R.string.settings_units), Units.KG.toString());
        } else if (unitsRadioGroup.getCheckedRadioButtonId() == R.id.lb_radio_button) {
            sharedPreferencesEditor.putString(getString(R.string.settings_units), Units.LB.toString());
        }
        sharedPreferencesEditor.apply();
    }

}
