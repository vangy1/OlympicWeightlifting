package com.olympicweightlifting.features.records.personal;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by vangor on 11/02/2018.
 */

@IgnoreExtraProperties
public class RecordsPersonalData {
    @Exclude
    private String documentId;

    private Date dateAdded;

    private double weight;
    private String units;
    private int reps;
    private String exercise;
    private Date date;

    public RecordsPersonalData() {
    }

    RecordsPersonalData(double weight, String units, int reps, String exercise, Date date) {
        this.weight = weight;
        this.units = units;
        this.reps = reps;
        this.exercise = exercise;
        this.date = date;
    }

    <T extends RecordsPersonalData> T withId(String documentId) {
        this.documentId = documentId;
        return (T) this;
    }

    @Exclude
    String getDocumentId() {
        return documentId;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public double getWeight() {
        return weight;
    }

    String getWeightFormatted() {
        return new DecimalFormat("##.##").format(weight);
    }

    public int getReps() {
        return reps;
    }

    public boolean validateObject() {
        return getUnits() != null &&
                getExercise() != null &&
                getDate() != null;
    }

    public String getUnits() {
        return units;
    }

    public String getExercise() {
        return exercise;
    }

    public Date getDate() {
        return date;
    }

    public void setDateAdded() {
        dateAdded = new Date();
    }
}
