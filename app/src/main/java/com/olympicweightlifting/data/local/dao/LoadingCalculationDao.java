package com.olympicweightlifting.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.olympicweightlifting.features.calculators.loading.LoadingCalculation;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface LoadingCalculationDao extends CalculationDao<LoadingCalculation> {
    @Query("SELECT * FROM loadingcalculation ORDER by id DESC LIMIT :limit ")
    Maybe<List<LoadingCalculation>> get(int limit);
}