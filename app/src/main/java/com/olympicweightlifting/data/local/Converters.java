package com.olympicweightlifting.data.local;

import android.arch.persistence.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vangor on 01/02/2018.
 */

public class Converters {

    @TypeConverter
    public String JSONArrayFromIntArray(List<Integer> values) {
        return new JSONArray(values).toString();
    }

    @TypeConverter
    public List<Integer> JSONArrayToIntArray(String values) throws JSONException {
        JSONArray jsonArray = new JSONArray(values);
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(Integer.parseInt(jsonArray.getString(i)));
        }
        return list;
    }
}