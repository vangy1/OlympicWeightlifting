package com.olympicweightlifting.mainpage;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.olympicweightlifting.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsDialog extends DialogFragment {
    @BindView(R.id.dialog_title)
    TextView dialogTitle;
    @BindView(R.id.dark_theme_switch)
    Switch darkThemeSwitch;
    @BindView(R.id.units_radio_group)
    RadioGroup unitsRadioGroup;

    private SharedPreferences sharedPreferences;

    public enum Units {
        KG, LB
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.settings_shared_preferences_id), Context.MODE_PRIVATE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_settings, null);
        ButterKnife.bind(this, dialogView);

        Typeface montserratBold = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold));
        dialogTitle.setTypeface(montserratBold);

        displayCurrentSettings();

        return new AlertDialog.Builder(getActivity()).setView(dialogView)
                .setPositiveButton(R.string.settings_positive_button_text, (dialog, id) -> saveSettingsToSharedPreferences()).create();
    }

    private void displayCurrentSettings() {
        darkThemeSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.settings_dark_theme), false));
        Units units = Units.valueOf(sharedPreferences.getString(getString(R.string.settings_units), Units.KG.toString()));
        if(units == Units.KG){
            unitsRadioGroup.check(R.id.kg_radio_button);
        } else if(units == Units.LB){
            unitsRadioGroup.check(R.id.lb_radio_button);
        }
    }

    private void saveSettingsToSharedPreferences() {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putBoolean(getString(R.string.settings_dark_theme), darkThemeSwitch.isChecked());
        if(unitsRadioGroup.getCheckedRadioButtonId() == R.id.kg_radio_button){
            sharedPreferencesEditor.putString(getString(R.string.settings_units), Units.KG.toString());
        } else if(unitsRadioGroup.getCheckedRadioButtonId() == R.id.lb_radio_button){
            sharedPreferencesEditor.putString(getString(R.string.settings_units), Units.LB.toString());
        }
        sharedPreferencesEditor.apply();
    }

}
