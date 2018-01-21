package com.olympicweightlifting.mainpage;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
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
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.settings_shared_preferences_id), Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialog_view = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_settings, null);
        ButterKnife.bind(this, dialog_view);

        Typeface montserratBold = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_path_montserrat_bold));
        dialogTitle.setTypeface(montserratBold);

        displayCurrentSettings();

        return new AlertDialog.Builder(getActivity()).setView(dialog_view)
                .setPositiveButton(R.string.settings_positive_button_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveSettingsToSharedPreferences();
                    }
                }).create();
    }

    private void displayCurrentSettings() {
        darkThemeSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.settings_dark_theme), false));
        unitsRadioGroup.check(sharedPreferences.getInt(getString(R.string.settings_units), R.id.kg));
    }

    private void saveSettingsToSharedPreferences() {
        sharedPreferencesEditor.putBoolean(getString(R.string.settings_dark_theme), darkThemeSwitch.isChecked());
        sharedPreferencesEditor.putInt(getString(R.string.settings_units), unitsRadioGroup.getCheckedRadioButtonId());
        sharedPreferencesEditor.apply();
    }

}
