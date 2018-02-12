package com.olympicweightlifting.utilities;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.Objects;

/**
 * Created by vangor on 01/02/2018.
 */

public class EditTextInputFilter implements InputFilter {
    private int min, max;

    public EditTextInputFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned destination, int dstart, int dend) {
        try {
            String newValue = destination.toString().substring(0, dstart) + destination.toString().substring(dend, destination.toString().length());
            newValue = newValue.substring(0, dstart) + source.toString() + newValue.substring(dstart, newValue.length());
            if (newValue.equalsIgnoreCase("-") && min < 0) return null;
            if (!Objects.equals(newValue, "")) {
                int input = Integer.parseInt(newValue);
                if (isInRange(min, max, input))
                    return null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
