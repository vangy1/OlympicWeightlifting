package com.olympicweightlifting;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculation;
import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculationDao;

@Database(entities = {SinclairCalculation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SinclairCalculationDao sinclairCalculationDao();
}
