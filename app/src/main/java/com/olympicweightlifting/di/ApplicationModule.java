package com.olympicweightlifting.di;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.olympicweightlifting.App;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.calculators.CalculatorsService;
import com.olympicweightlifting.features.helpers.exercisemanager.Exercise;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.olympicweightlifting.utilities.ApplicationConstants.DATBASE_NAME;
import static com.olympicweightlifting.utilities.ApplicationConstants.PREF_APP_INFO;
import static com.olympicweightlifting.utilities.ApplicationConstants.PREF_SETTINGS;

@Module
public abstract class ApplicationModule {
    @Provides
    @Singleton
    static Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    @Named("app-info")
    static SharedPreferences provideAppInfoSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_APP_INFO, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    @Named("settings")
    static SharedPreferences provideSettingsSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    static AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, DATBASE_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Completable.fromAction(() -> {
                            provideDatabase(context).exerciseDao().insert(new Exercise("Snatch"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Clean & Jerk"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Squat"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Deadlift"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Front Squat"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Overhead Squat"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Clean"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Jerk"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Snatch Deadlift"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Overhead Press"));
                            provideDatabase(context).exerciseDao().insert(new Exercise("Bench Press"));
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                    }
                })
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    static CalculatorsService provideCalculatorService(Context context, @Named("settings") SharedPreferences sharedPreferences) {
        return new CalculatorsService(context, sharedPreferences);
    }
}
