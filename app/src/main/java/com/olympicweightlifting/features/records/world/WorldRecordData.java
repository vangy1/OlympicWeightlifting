package com.olympicweightlifting.features.records.world;

import java.util.Date;

/**
 * Created by vangor on 07/02/2018.
 */

public class WorldRecordData {
    private String recordHolderName;
    private int weightLiftedInKg;
    private Date dateOfRecord;

    // empty constructor is needed for constructing object from Firebase data
    public WorldRecordData() {
    }

    public WorldRecordData(String recordHolderName, int weightLiftedInKg, Date dateOfRecord) {
        this.recordHolderName = recordHolderName;
        this.weightLiftedInKg = weightLiftedInKg;
        this.dateOfRecord = dateOfRecord;
    }


    public String getRecordHolderName() {
        return recordHolderName;
    }

    public int getWeightLiftedInKg() {
        return weightLiftedInKg;
    }

    public Date getDateOfRecord() {
        return dateOfRecord;
    }
}
