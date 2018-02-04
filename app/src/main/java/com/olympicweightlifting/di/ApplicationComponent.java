package com.olympicweightlifting.di;

import com.olympicweightlifting.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {BindingModule.class, ApplicationModule.class, AndroidSupportInjectionModule.class})
public interface ApplicationComponent extends AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);

        ApplicationComponent build();
    }

}