package com.olympicweightlifting.utilities;

/**
 * Created by vangor on 07/02/2018.
 */

public class ApplicationConstants {
    public static final String DATBASE_NAME = "DATABASE";
    public static final String FIREBASE_COLLECTION_USERS = "users";
    public static final String FIREBASE_COLLECTION_PROGRAMS = "programs";
    public static final String FIREBASE_COLLECTION_RECORDS_PERSONAL = "personal_records";
    public static final String FIREBASE_COLLECTION_RECORDS_WORLD = "world_records";
    public static final String FIREBASE_COLLECTION_WORKOUTS_TRACKED = "tracked_workouts";
    public static final String PREF_UNITS = "PREF_UNITS";

    public enum Gender {
        MEN, WOMEN
    }

    public enum Units {
        KG, LB
    }


}
