package com.olympicweightlifting.di;


import com.olympicweightlifting.features.calculators.CalculatorsActivity;
import com.olympicweightlifting.features.calculators.loading.LoadingCalculatorFragment;
import com.olympicweightlifting.features.calculators.repmax.RepmaxCalculatorFragment;
import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculatorFragment;
import com.olympicweightlifting.features.helpers.exercisemanager.ExerciseManagerDialog;
import com.olympicweightlifting.features.programs.create.NewProgramFragment;
import com.olympicweightlifting.features.records.personal.RecordsPersonalFragment;
import com.olympicweightlifting.features.tracking.tracknew.TrackingNewFragment;
import com.olympicweightlifting.mainpage.MainActivity;
import com.olympicweightlifting.mainpage.SettingsDialog;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BindingModule {
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract SettingsDialog bindSettingsDialog();

    @ContributesAndroidInjector
    abstract CalculatorsActivity bindCalculatorActivity();

    @ContributesAndroidInjector
    abstract SinclairCalculatorFragment bindSinclairCalculatorFragment();

    @ContributesAndroidInjector
    abstract RepmaxCalculatorFragment bindRepmaxCalculatorFragment();

    @ContributesAndroidInjector
    abstract LoadingCalculatorFragment bindLoadingCalculatorFragment();

    @ContributesAndroidInjector
    abstract RecordsPersonalFragment bindRecordsPersonalFragment();

    @ContributesAndroidInjector
    abstract TrackingNewFragment bindTrackingNewFragment();

    @ContributesAndroidInjector
    abstract ExerciseManagerDialog bindExerciseManagerDialog();

    @ContributesAndroidInjector
    abstract NewProgramFragment bindNewProgramFragment();
}
