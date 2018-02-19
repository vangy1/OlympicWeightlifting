package com.olympicweightlifting.features.helpers.exercisemanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"exercise_name"},
        unique = true)})
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "exercise_name")
    @NonNull
    public String exerciseName;

    public Exercise(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseName() {
        return exerciseName;
    }
}
