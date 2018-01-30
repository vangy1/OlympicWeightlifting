package com.olympicweightlifting.di;

import android.app.Application;
import android.content.Context;

import com.olympicweightlifting.MyApplication;
import com.olympicweightlifting.calculators.Calculator;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(Application application);

    @Provides
    static Calculator provideCalculator(MyApplication application) {
        return new Calculator(application.getApplicationContext());
    }
}
