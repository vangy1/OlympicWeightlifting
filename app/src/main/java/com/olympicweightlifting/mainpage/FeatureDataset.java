package com.olympicweightlifting.mainpage;

import android.content.Intent;

/**
 * Created by vangor on 16/01/2018.
 */

public class FeatureDataset {
    private String featureName;
    private String[] featureShortcuts;
    private int featureImage;
    private Intent activityToStart;

    public FeatureDataset(String featureName, String[] featureShortcuts, int featureImage, Intent activityToStart) {
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

    public Intent getActivityToStart() {
        return activityToStart;
    }
}
