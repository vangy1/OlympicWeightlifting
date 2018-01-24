package com.olympicweightlifting.di;

import android.content.Context;

import com.olympicweightlifting.App;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }
}
