package com.olympicweightlifting.features.calculators.sinclair;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by vangor on 31/01/2018.
 */

@Dao
public interface SinclairCalculationDao {
    @Query("SELECT * FROM sinclairCalculation ORDER by id DESC LIMIT :limit ")
    Maybe<List<SinclairCalculation>> get(int limit);

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);


    @Insert
    void insert(SinclairCalculation sinclairCalculation);

//    @Insert
//    void insertAll(User... users);

    @Delete
    void delete(SinclairCalculation sinclairCalculation);
}
