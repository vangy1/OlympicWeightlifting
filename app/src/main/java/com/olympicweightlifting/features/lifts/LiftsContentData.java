package com.olympicweightlifting.features.lifts;

/**
 * Created by vangor on 04/02/2018.
 */

public class LiftsContentData {
    private String title;
    private String description;
    private String videoUrl;

    LiftsContentData(String title, String description, String videoUrl) {
        this(title, description);
        this.videoUrl = videoUrl;
    }

    LiftsContentData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    String getVideoUrl() {
        return videoUrl;
    }
}
