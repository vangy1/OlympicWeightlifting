package com.olympicweightlifting.features.tracking.data;

import java.text.DecimalFormat;

public class TrackedExerciseData {
    private double weight;
    private String units;
    private int reps;
    private int sets;
    private String exercise;

    public TrackedExerciseData() {
    }

    public TrackedExerciseData(double weight, String units, int reps, int sets, String exercise) {
        this.weight = weight;
        this.units = units;
        this.reps = reps;
        this.sets = sets;
        this.exercise = exercise;
    }

    public double getWeight() {
        return weight;
    }

    public String getWeightFormatted() {
        return new DecimalFormat("##.##").format(weight);
    }


    public String getUnits() {
        return units;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public String getExercise() {
        return exercise;
    }
}
