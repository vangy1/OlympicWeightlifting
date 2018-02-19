package com.olympicweightlifting.features.calculators.repmax;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity
public class RepmaxCalculation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public List<Integer> results;
    public String type;
    public String units;

    public RepmaxCalculation(List<Integer> results, String type, String units) {
        this.results = results;
        this.type = type;
        this.units = units;
    }

    public List<Integer> getResults() {
        return results;
    }

    public String getType() {
        return type;
    }

    public String getUnits() {
        return units;
    }
}
