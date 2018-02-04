package com.olympicweightlifting.data.local.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

public interface CalculationDao<T> {

    // TODO: move method "get" from extended classes here when Android Room allows generics in @Query

    @Insert
    void insert(T calculation);

    @Delete
    void delete(T calculation);
}
