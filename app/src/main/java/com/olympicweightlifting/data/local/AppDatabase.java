package com.olympicweightlifting.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.olympicweightlifting.data.local.dao.ExerciseDao;
import com.olympicweightlifting.data.local.dao.LoadingCalculationDao;
import com.olympicweightlifting.data.local.dao.RepMaxCalculationDao;
import com.olympicweightlifting.data.local.dao.SinclairCalculationDao;
import com.olympicweightlifting.features.calculators.loading.LoadingCalculation;
import com.olympicweightlifting.features.calculators.repmax.RepmaxCalculation;
import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculation;
import com.olympicweightlifting.features.helpers.exercisemanager.Exercise;

@Database(entities = {SinclairCalculation.class, RepmaxCalculation.class, LoadingCalculation.class, Exercise.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract SinclairCalculationDao sinclairCalculationDao();

    public abstract RepMaxCalculationDao repmaxCalculationDao();

    public abstract LoadingCalculationDao loadingCalculationDao();

    public abstract ExerciseDao exerciseDao();
}
