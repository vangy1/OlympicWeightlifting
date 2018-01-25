package com.olympicweightlifting.calculators.sinclair;

import com.olympicweightlifting.calculators.Calculator.Gender;

/**
 * Created by vangor on 25/01/2018.
 */

public class SinclairCalculationDataset {
    private double total;
    private double bodyweight;
    private Gender gender;
    private double sinclairScore;

    public SinclairCalculationDataset(double total, double bodyweight, Gender gender, double sinclairScore) {
        this.total = total;
        this.bodyweight = bodyweight;
        this.gender = gender;
        this.sinclairScore = sinclairScore;
    }

    public double getTotal() {
        return total;
    }

    public double getBodyweight() {
        return bodyweight;
    }

    public Gender getGender() {
        return gender;
    }

    public double getSinclairScore() {
        return sinclairScore;
    }
}
