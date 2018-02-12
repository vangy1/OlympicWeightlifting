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

//    private static AppDatabase INSTANCE;
//
//    public synchronized static AppDatabase getInstance(Context context) {
//        if (INSTANCE == null) {
//            INSTANCE = provideDatabase(context);
//        }
//        return INSTANCE;
//    }
//
//    public static AppDatabase provideDatabase(Context context) {
//        return Room.databaseBuilder(context,
//                AppDatabase.class, context.getString(R.string.database_name))
//                .addCallback(new RoomDatabase.Callback() {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//
//                        Completable.fromAction(() -> {
//                            getInstance(context).exerciseDao().insert(new Exercise("LoL"));
//                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
//                    }
//                })
//                .fallbackToDestructiveMigration()
//                .build();
//    }

    public abstract SinclairCalculationDao sinclairCalculationDao();

    public abstract RepMaxCalculationDao repmaxCalculationDao();

    public abstract LoadingCalculationDao loadingCalculationDao();

    public abstract ExerciseDao exerciseDao();
}
