package com.olympicweightlifting.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.olympicweightlifting.features.helpers.exercisemanager.Exercise;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by vangor on 09/02/2018.
 */

@Dao
public interface ExerciseDao {

    @Query("DELETE FROM exercise WHERE exercise_name = :exerciseName ")
    void deleteByExerciseName(String exerciseName);

    @Insert
    void insert(Exercise exercise);

    @Query("SELECT * FROM exercise")
    Maybe<List<Exercise>> getAll();
}
