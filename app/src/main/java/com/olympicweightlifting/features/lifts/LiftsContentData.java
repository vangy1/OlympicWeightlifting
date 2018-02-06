package com.olympicweightlifting.features.lifts;

/**
 * Created by vangor on 04/02/2018.
 */

public class LiftsContentData {
    private String title;
    private String description;
    private String videoUrl;

    public LiftsContentData(String title, String description, String videoUrl) {
        this(title, description);
        this.videoUrl = videoUrl;
    }

    public LiftsContentData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
