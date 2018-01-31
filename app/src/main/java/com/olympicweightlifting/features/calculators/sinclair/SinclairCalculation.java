package com.olympicweightlifting.features.calculators.sinclair;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
    public double sinclairScore;

    public SinclairCalculation(double total, double bodyweight, String gender, double sinclairScore) {
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

    public String getGender() {
        return gender;
    }

    public double getSinclairScore() {
        return sinclairScore;
    }
}
