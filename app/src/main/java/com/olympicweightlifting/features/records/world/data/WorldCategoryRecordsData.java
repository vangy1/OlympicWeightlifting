package com.olympicweightlifting.features.records.world.data;

import com.olympicweightlifting.utilities.AppLevelConstants.Gender;

/**
 * Created by vangor on 07/02/2018.
 */

public class WorldCategoryRecordsData {
    public Gender gender;
    public String weightCategory;
    public WorldRecordData snatchRecord;
    public WorldRecordData cajRecord;
    public WorldRecordData totalRecord;
    private int id;

    // empty constructor is needed for constructing object from Firebase data
    public WorldCategoryRecordsData() {
    }

    public WorldCategoryRecordsData(int id, String gender, String weightCategory, WorldRecordData snatchRecord, WorldRecordData cajRecord, WorldRecordData totalRecord) {
        this.id = id;
        this.gender = Gender.valueOf(gender);
        this.weightCategory = weightCategory;
        this.snatchRecord = snatchRecord;
        this.cajRecord = cajRecord;
        this.totalRecord = totalRecord;
    }

    public int getId() {
        return id;
    }

    public boolean validateObject() {
        return getWeightCategory() != null &&
                getGender() != null &&
                getSnatchRecord() != null &&
                getCajRecord() != null &&
                getTotalRecord() != null;
    }

    public String getWeightCategory() {
        return weightCategory;
    }

    public String getGender() {
        return gender.toString();
    }

    public WorldRecordData getSnatchRecord() {
        return snatchRecord;
    }

    public WorldRecordData getCajRecord() {
        return cajRecord;
    }

    public WorldRecordData getTotalRecord() {
        return totalRecord;
    }
}
