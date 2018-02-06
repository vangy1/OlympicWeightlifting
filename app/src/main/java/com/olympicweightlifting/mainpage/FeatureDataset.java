package com.olympicweightlifting.mainpage;

import android.os.Bundle;

/**
 * Created by vangor on 16/01/2018.
 */

public class FeatureDataset {
    private String featureName;
    private String[] featureShortcuts;
    private int featureImage;
    private Class activityToStart;
    private Bundle activityArguments;

    public FeatureDataset(String featureName, String[] featureShortcuts, int featureImage, Class activityToStart, Bundle activityArguments) {
        this(featureName, featureShortcuts, featureImage, activityToStart);
        this.activityArguments = activityArguments;
    }

    public FeatureDataset(String featureName, String[] featureShortcuts, int featureImage, Class activityToStart) {
        this.featureName = featureName;
        this.featureShortcuts = featureShortcuts;
        this.featureImage = featureImage;
        this.activityToStart = activityToStart;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String[] getFeatureShortcuts() {
        return featureShortcuts;
    }

    public int getFeatureImage() {
        return featureImage;
    }

    public Class getActivityToStart() {
        return activityToStart;
    }

    public Bundle getActivityArguments() {
        return activityArguments;
    }
}
