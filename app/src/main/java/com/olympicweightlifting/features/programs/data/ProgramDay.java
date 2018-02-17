package com.olympicweightlifting.features.programs.data;

import java.util.ArrayList;
import java.util.List;

public class ProgramDay {

    private List<ProgramExercise> exercises = new ArrayList<>();

    public ProgramDay() {
    }

    public ProgramDay(List<ProgramExercise> exercises) {
        this.exercises = exercises;
    }

    public List<ProgramExercise> getExercises() {
        return exercises;
    }
}
