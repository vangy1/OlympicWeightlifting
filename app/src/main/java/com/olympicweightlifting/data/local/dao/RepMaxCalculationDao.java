package com.olympicweightlifting.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.olympicweightlifting.features.calculators.repmax.RepmaxCalculation;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface RepMaxCalculationDao extends CalculationDao<RepmaxCalculation> {
    @Query("SELECT * FROM repmaxcalculation ORDER by id DESC LIMIT :limit ")
    Maybe<List<RepmaxCalculation>> get(int limit);
}
