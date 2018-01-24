package com.olympicweightlifting.di;

import com.olympicweightlifting.calculators.loading.LoadingCalculator;
import com.olympicweightlifting.calculators.repmax.RepmaxCalculator;
import com.olympicweightlifting.calculators.sinclair.SinclairCalculator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class CalculatorsModule {
    @Singleton
    @Provides
    SinclairCalculator provideSinclairCalculator() {
        return new SinclairCalculator();
    }

    @Singleton
    @Provides
    RepmaxCalculator provideRepmaxCalculator() {
        return new RepmaxCalculator();
    }

    @Singleton
    @Provides
    LoadingCalculator provideLoadingCalculator() {
        return new LoadingCalculator();
    }
}
