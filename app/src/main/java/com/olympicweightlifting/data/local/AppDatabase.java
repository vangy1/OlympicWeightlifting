package com.olympicweightlifting.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.olympicweightlifting.data.local.dao.LoadingCalculationDao;
import com.olympicweightlifting.data.local.dao.RepMaxCalculationDao;
import com.olympicweightlifting.data.local.dao.SinclairCalculationDao;
import com.olympicweightlifting.features.calculators.loading.LoadingCalculation;
import com.olympicweightlifting.features.calculators.repmax.RepmaxCalculation;
import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculation;

@Database(entities = {SinclairCalculation.class, RepmaxCalculation.class, LoadingCalculation.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract SinclairCalculationDao sinclairCalculationDao();

    public abstract RepMaxCalculationDao repmaxCalculationDao();

    public abstract LoadingCalculationDao loadingCalculationDao();
}
