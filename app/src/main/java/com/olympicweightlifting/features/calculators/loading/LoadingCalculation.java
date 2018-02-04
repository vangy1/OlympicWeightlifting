package com.olympicweightlifting.features.calculators.loading;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by vangor on 03/02/2018.
 */

@Entity
public class LoadingCalculation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int weight;
    public int barbell;
    public boolean collars;
    public List<Integer> results;

    public LoadingCalculation(int weight, int barbell, boolean collars, List<Integer> results) {
        this.weight = weight;
        this.barbell = barbell;
        this.collars = collars;
        this.results = results;
    }

    public int getWeight() {
        return weight;
    }

    public int getBarbell() {
        return barbell;
    }

    public boolean hasCollars() {
        return collars;
    }

    public List<Integer> getResults() {
        return results;
    }
}
