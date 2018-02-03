package com.olympicweightlifting.di;


import com.olympicweightlifting.features.calculators.CalculatorsActivity;
import com.olympicweightlifting.features.calculators.repmax.RepmaxCalculatorFragment;
import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculatorFragment;
import com.olympicweightlifting.mainpage.SettingsDialog;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BindingModule {
    @ContributesAndroidInjector
    abstract SettingsDialog bindSettingsDialog();

    @ContributesAndroidInjector
    abstract CalculatorsActivity bindCalculatorActivity();

    @ContributesAndroidInjector
    abstract SinclairCalculatorFragment bindSinclairCalculatorFragment();

    @ContributesAndroidInjector
    abstract RepmaxCalculatorFragment bindRepmaxCalculatorFragment();
}
