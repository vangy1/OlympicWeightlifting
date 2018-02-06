package com.olympicweightlifting.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import com.olympicweightlifting.App;
import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.calculators.CalculatorService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ApplicationModule {
    @Provides
    @Singleton
    static Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    @Named("settings")
    static SharedPreferences provideSettingsSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.settings_shared_preferences_id), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    static AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, context.getString(R.string.database_name))
                .fallbackToDestructiveMigration()
                .build();
    }
//
//    @Provides
//    @Singleton
//    static LiftsContentDataUtility provideLiftsService(Context context) {
//        return new LiftsContentDataUtility(context);
//    }

    @Provides
    @Singleton
    static CalculatorService provideCalculatorService(Context context, @Named("settings") SharedPreferences sharedPreferences) {
        return new CalculatorService(context, sharedPreferences);
    }
}
