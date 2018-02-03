package com.olympicweightlifting.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.olympicweightlifting.features.calculators.sinclair.SinclairCalculation;

import java.util.List;

import io.reactivex.Maybe;


@Dao
public interface SinclairCalculationDao extends CalculationDao<SinclairCalculation> {
    @Query("SELECT * FROM sinclaircalculation ORDER by id DESC LIMIT :limit ")
    Maybe<List<SinclairCalculation>> get(int limit);
}

