package com.olympicweightlifting.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.olympicweightlifting.App;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.calculators.Calculator;

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
    static Calculator provideCalculator(Context context, @Named("settings") SharedPreferences sharedPreferences) {
        return new Calculator(context, sharedPreferences);
    }


}
