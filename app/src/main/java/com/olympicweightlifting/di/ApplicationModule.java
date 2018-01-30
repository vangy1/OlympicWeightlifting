package com.olympicweightlifting.di;

import android.app.Application;
import android.content.Context;

import com.olympicweightlifting.App;
import com.olympicweightlifting.features.calculators.Calculator;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(Application application);

    @Provides
    static Calculator provideCalculator(App application) {
        return new Calculator(application.getApplicationContext());
    }
}
