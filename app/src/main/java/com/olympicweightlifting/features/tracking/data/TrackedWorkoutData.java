package com.olympicweightlifting.features.tracking.data;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class TrackedWorkoutData {
    private List<TrackedExerciseData> trackedExercises;
    private Date dateOfWorkout;
    @Exclude
    private String documentId;
    private Date dateAdded;

    public TrackedWorkoutData() {
    }

    public TrackedWorkoutData(List<TrackedExerciseData> trackedExercises, Date dateOfWorkout) {
        this.trackedExercises = trackedExercises;
        this.dateOfWorkout = dateOfWorkout;
    }

    public <T extends TrackedWorkoutData> T withId(String documentId) {
        this.documentId = documentId;
        return (T) this;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public boolean validateObject() {
        return getTrackedExercises() != null &&
                getDateOfWorkout() != null &&
                getDateAdded() != null;
    }

    public List<TrackedExerciseData> getTrackedExercises() {
        return trackedExercises;
    }

    public Date getDateOfWorkout() {
        return dateOfWorkout;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded() {
        this.dateAdded = new Date();
    }
}