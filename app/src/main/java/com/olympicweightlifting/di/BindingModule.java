package com.olympicweightlifting.di;


import com.olympicweightlifting.authentication.SignInActivity;
import com.olympicweightlifting.authentication.profile.ProfileActivity;
import com.olympicweightlifting.features.calculators.CalculatorsActivity;
import com.olympicweightlifting.features.calculators.loading.LoadingCalculatorFragment;
import com.olympicweightlifting.features.calculators.repmax.RepmaxCalculatorFragment;
import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculatorFragment;
import com.olympicweightlifting.features.helpers.exercisemanager.ExerciseManagerDialog;
import com.olympicweightlifting.features.lifts.LiftsActivity;
import com.olympicweightlifting.features.programs.ProgramsActivity;
import com.olympicweightlifting.features.programs.create.ProgramCreateFragment;
import com.olympicweightlifting.features.records.RecordsActivity;
import com.olympicweightlifting.features.records.personal.RecordsPersonalFragment;
import com.olympicweightlifting.features.tracking.TrackingActivity;
import com.olympicweightlifting.features.tracking.tracknew.TrackingNewFragment;
import com.olympicweightlifting.mainpage.MainActivity;
import com.olympicweightlifting.mainpage.SettingsDialog;
import com.olympicweightlifting.mainpage.WelcomeDialog;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BindingModule {
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract WelcomeDialog bindWelcomeDialog();

    @ContributesAndroidInjector
    abstract SignInActivity bindSignInActivity();

    @ContributesAndroidInjector
    abstract SettingsDialog bindSettingsDialog();

    @ContributesAndroidInjector
    abstract ProfileActivity bindProfileActivity();

    @ContributesAndroidInjector
    abstract LiftsActivity bindLiftsActivity();

    @ContributesAndroidInjector
    abstract CalculatorsActivity bindCalculatorActivity();

    @ContributesAndroidInjector
    abstract SinclairCalculatorFragment bindSinclairCalculatorFragment();

    @ContributesAndroidInjector
    abstract RepmaxCalculatorFragment bindRepmaxCalculatorFragment();

    @ContributesAndroidInjector
    abstract LoadingCalculatorFragment bindLoadingCalculatorFragment();

    @ContributesAndroidInjector
    abstract ProgramsActivity bindProgramsActivity();

    @ContributesAndroidInjector
    abstract ProgramCreateFragment bindNewProgramFragment();

    @ContributesAndroidInjector
    abstract TrackingActivity bindTrackingActivity();

    @ContributesAndroidInjector
    abstract TrackingNewFragment bindTrackingNewFragment();

    @ContributesAndroidInjector
    abstract RecordsActivity bindRecordsActivity();

    @ContributesAndroidInjector
    abstract RecordsPersonalFragment bindRecordsPersonalFragment();

    @ContributesAndroidInjector
    abstract ExerciseManagerDialog bindExerciseManagerDialog();
}
