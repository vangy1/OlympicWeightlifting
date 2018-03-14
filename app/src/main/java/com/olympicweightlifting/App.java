package com.olympicweightlifting;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.olympicweightlifting.di.DaggerApplicationComponent;
import com.olympicweightlifting.utilities.AnalyticsTracker;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class App extends DaggerApplication {
    private static GoogleAnalytics analytics;
    private static AnalyticsTracker analyticsTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        analyticsTracker = new AnalyticsTracker(analytics.newTracker(R.xml.global_tracker));
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }

    synchronized public AnalyticsTracker getAnalyticsTracker() {
        if (analyticsTracker.getTracker() == null) {
            analyticsTracker.setTracker(analytics.newTracker(R.xml.global_tracker));
        }

        return analyticsTracker;
    }
}
