package com.olympicweightlifting.mainpage;

/**
 * Created by vangor on 16/01/2018.
 */

public class FeatureDataset {
    private String featureName;
    private String[] featureShortcuts;
    private int featureImage;

    public FeatureDataset(String featureName, String[] featureShortcuts, int featureImage) {
        this.featureName = featureName;
        this.featureShortcuts = featureShortcuts;
        this.featureImage = featureImage;
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
}
