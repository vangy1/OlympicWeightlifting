package com.olympicweightlifting.features.lifts;

import java.util.List;


class LiftsFragmentData {
    private List<LiftsContentData> liftsContentDataList;
    private String floatingButtonVideoUrl;

    LiftsFragmentData(List<LiftsContentData> liftsContentDataList, String floatingButtonVideoUrl) {
        this(liftsContentDataList);
        this.floatingButtonVideoUrl = floatingButtonVideoUrl;
    }

    LiftsFragmentData(List<LiftsContentData> liftsContentDataList) {
        this.liftsContentDataList = liftsContentDataList;
    }

    List<LiftsContentData> getLiftsContentDataList() {
        return liftsContentDataList;
    }

    String getFloatingButtonVideoUrl() {
        return floatingButtonVideoUrl;
    }
}
