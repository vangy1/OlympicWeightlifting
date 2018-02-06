package com.olympicweightlifting.features.lifts;

import java.util.List;

/**
 * Created by vangor on 06/02/2018.
 */

public class LiftsFragmentData {
    private List<LiftsContentData> liftsContentDataList;
    private String floatingButtonVideoUrl;

    public LiftsFragmentData(List<LiftsContentData> liftsContentDataList, String floatingButtonVideoUrl) {
        this(liftsContentDataList);
        this.floatingButtonVideoUrl = floatingButtonVideoUrl;
    }

    public LiftsFragmentData(List<LiftsContentData> liftsContentDataList) {
        this.liftsContentDataList = liftsContentDataList;
    }

    public List<LiftsContentData> getLiftsContentDataList() {
        return liftsContentDataList;
    }

    public String getFloatingButtonVideoUrl() {
        return floatingButtonVideoUrl;
    }
}
