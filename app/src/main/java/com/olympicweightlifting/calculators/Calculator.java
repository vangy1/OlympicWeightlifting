package com.olympicweightlifting.calculators;

import android.content.Context;
import android.content.SharedPreferences;

import com.olympicweightlifting.R;
import com.olympicweightlifting.mainpage.SettingsDialog.Units;

import javax.inject.Inject;

import static java.lang.Math.log10;
import static java.lang.Math.pow;

public class Calculator {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public enum Gender {
        MALE, FEMALE
    }

    @Inject
    public Calculator(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings_shared_preferences_id), Context.MODE_PRIVATE);
    }

    public double calculateSinclair(double total, double bodyweight, Gender gender) {
        Units units = Units.valueOf(sharedPreferences.getString(context.getString(R.string.settings_units), Units.KG.toString()));
        if(units == Units.LB){
            total /= 2.268;
            bodyweight /= 2.268;
        }

        if(gender == Gender.MALE){
            return total * pow(10, 0.751945030 * pow(log10(bodyweight / 175.508), 2));
        } else {
            return total * pow(10, 0.783497476 * pow(log10(bodyweight / 153.655), 2));
        }
    }
}
