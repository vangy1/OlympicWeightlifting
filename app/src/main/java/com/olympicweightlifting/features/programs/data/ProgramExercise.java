package com.olympicweightlifting.features.programs.data;

/**
 * Created by vangor on 14/02/2018.
 */

public class ProgramExercise {
    private String exerciseName;
    private int reps;
    private int sets;

    public ProgramExercise() {
    }

    public ProgramExercise(String exerciseName, int reps, int sets) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.sets = sets;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }
}
