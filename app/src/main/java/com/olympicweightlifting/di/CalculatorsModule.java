package com.olympicweightlifting.di;

import com.olympicweightlifting.App;
import com.olympicweightlifting.calculators.Calculator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class CalculatorsModule {
    @Singleton
    @Provides
    Calculator provideCalculator(App application) {
        return new Calculator(application.getApplicationContext());
    }
}
