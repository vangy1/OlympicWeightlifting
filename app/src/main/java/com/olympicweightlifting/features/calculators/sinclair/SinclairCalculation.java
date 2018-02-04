package com.olympicweightlifting.features.calculators.sinclair;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DecimalFormat;

/**
 * Created by vangor on 25/01/2018.
 */

@Entity
public class SinclairCalculation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public double total;
    public double bodyweight;
    public String gender;
    public String units;
    public double sinclairScore;

    public SinclairCalculation(double total, double bodyweight, String gender, String units, double sinclairScore) {
        this.total = total;
        this.bodyweight = bodyweight;
        this.gender = gender;
        this.units = units;
        this.sinclairScore = sinclairScore;
    }

    public double getTotal() {
        return total;
    }

    public String getTotalFormatted() {
        return formatDoubleToString(total);
    }

    private String formatDoubleToString(double bodyweight) {
        return new DecimalFormat("##.##").format(bodyweight);
    }

    public double getBodyweight() {
        return bodyweight;
    }

    public String getBodyweightFormatted() {
        return formatDoubleToString(bodyweight);
    }

    public String getGender() {
        return gender;
    }

    public String getUnits() {
        return units;
    }

    public double getSinclairScore() {
        return sinclairScore;
    }

}
