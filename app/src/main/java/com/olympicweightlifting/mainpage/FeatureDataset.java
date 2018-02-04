package com.olympicweightlifting.mainpage;

/**
 * Created by vangor on 16/01/2018.
 */

public class FeatureDataset {
    private String featureName;
    private String[] featureShortcuts;
    private int featureImage;
    private Class activityToStart;

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
}
