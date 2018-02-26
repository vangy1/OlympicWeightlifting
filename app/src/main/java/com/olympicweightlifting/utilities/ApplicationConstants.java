package com.olympicweightlifting.utilities;

public class ApplicationConstants {
    public static final String DATBASE_NAME = "DATABASE";
    public static final String PREF_SETTINGS = "PREF_SETTINGS";
    public static final String PREF_SETTINGS_UNITS = "UNITS";
    public static final String PREF_APP_INFO = "PREF_APP_INFO";
    public static final String PREF_APP_INFO_IS_FIRST_RUN = "PREF_APP_INFO";

    public static final String FIREBASE_COLLECTION_USERS = "users";
    public static final String FIREBASE_COLLECTION_PROGRAMS = "programs";
    public static final String FIREBASE_COLLECTION_RECORDS_PERSONAL = "personal_records";
    public static final String FIREBASE_COLLECTION_RECORDS_WORLD = "world_records";
    public static final String FIREBASE_COLLECTION_WORKOUTS = "tracked_workouts";

    public static final int RECORDS_LIMIT = 20;
    public static final int WORKOUTS_LIMIT = 15;
    public static final int PROGRAMS_LIMIT = 5;


    public enum Gender {
        MEN, WOMEN
    }

    public enum Units {
        KG, LB
    }

    public enum UserProfileStatus {
        UNDEFINED, FREE, PREMIUM
    }



}
