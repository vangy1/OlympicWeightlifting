package com.olympicweightlifting.di;

import android.app.Application;

import com.olympicweightlifting.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = { BuildersModule.class, AppModule.class, CalculatorsModule.class })
public interface AppComponent extends AndroidInjector<Application> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);
        AppComponent build();
    }
    void inject(App app);
}