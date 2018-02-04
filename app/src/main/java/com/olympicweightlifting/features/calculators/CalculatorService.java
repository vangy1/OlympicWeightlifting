package com.olympicweightlifting.features.calculators;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.itemanimators.SlideRightAlphaAnimator;
import com.olympicweightlifting.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Math.log10;
import static java.lang.Math.pow;

public class CalculatorService {

    public final int HISTORY_MAX = 10;
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public enum Gender {
        MALE, FEMALE
    }

    public enum RepmaxType {
        REPS, PERCENTAGE
    }

    public enum Units {
        KG, LB
    }

    // TODO: move database dependency from individual calculator fragments into this service once Android Room allows generics in @Query
    @Inject
    public CalculatorService(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUnitsForViews(TextView... textViews) {
        String units = sharedPreferences.getString(context.getString(R.string.settings_units), Units.KG.toString()).toLowerCase();
        for (TextView textView : textViews) {
            textView.setText(units);
        }
    }

    public void setupResultsRecyclerView(RecyclerView resultsRecyclerView,
                                         RecyclerView.Adapter resultsRecyclerViewAdapter) {
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(resultsRecyclerView.getContext()));
        resultsRecyclerView.setAdapter(resultsRecyclerViewAdapter);
        resultsRecyclerView.setItemAnimator(new SlideRightAlphaAnimator());
    }

    public <T> void populateRecyclerViewFromDatabase(Maybe<List<T>> calculationsMaybe, List<T> calculations, RecyclerView resultsRecyclerView) {
        calculationsMaybe.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((queriedCalculations) -> {
            calculations.addAll(queriedCalculations);
            resultsRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    public <T> void insertCalculationIntoRecyclerView(T calculation, List<T> calculations, RecyclerView resultsRecyclerView) {
        if (calculations.size() >= HISTORY_MAX) {
            calculations.remove(calculations.size() - 1);
            resultsRecyclerView.getAdapter().notifyItemRangeRemoved(calculations.size(), 1);
        }
        calculations.add(0, calculation);
        resultsRecyclerView.getAdapter().notifyItemInserted(0);
        resultsRecyclerView.scrollToPosition(0);
    }

    public void getResultViewsFromLayout(ViewGroup resultsLayout, List<TextView> resultsTitleTextViews, List<TextView> resultsTextViews) {
        int childCount = resultsLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = resultsLayout.getChildAt(i);
            if (view.getTag() == null) continue;
            String viewTag = view.getTag().toString();
            if (view instanceof TextView) {
                if (Objects.equals(viewTag, "result_title")) {
                    resultsTitleTextViews.add((TextView) view);
                } else if (Objects.equals(viewTag, "result")) {
                    resultsTextViews.add((TextView) view);
                }
            }
        }
    }

    public double calculateSinclair(double total, double bodyweight, Gender gender) {
        Units units = Units.valueOf(sharedPreferences.getString(context.getString(R.string.settings_units), Units.KG.toString()));
        if (units == Units.LB) {
            total /= 2.268;
            bodyweight /= 2.268;
        }

        if (gender == Gender.MALE) {
            return total * pow(10, 0.751945030 * pow(log10(bodyweight / 175.508), 2));
        } else {
            return total * pow(10, 0.783497476 * pow(log10(bodyweight / 153.655), 2));
        }
    }

    public List<Integer> calculateRepmax(double weight, int reps, RepmaxType repmaxType) {
        double oneRepMax = weight / (1.0278 - (0.0278 * reps));
        List<Integer> results = new ArrayList<>();

        results.add((int) oneRepMax);
        if (repmaxType == RepmaxType.REPS) {
            results.add((int) Math.round(oneRepMax / 1.029));
            results.add((int) Math.round(oneRepMax / 1.059));
            results.add((int) Math.round(oneRepMax / 1.091));
            results.add((int) Math.round(oneRepMax / 1.125));
            results.add((int) Math.round(oneRepMax / 1.161));
            results.add((int) Math.round(oneRepMax / 1.200));
            results.add((int) Math.round(oneRepMax / 1.242));
            results.add((int) Math.round(oneRepMax / 1.286));
            results.add((int) Math.round(oneRepMax / 1.330));
        } else {
            results.add((int) Math.round(oneRepMax * 0.95));
            results.add((int) Math.round(oneRepMax * 0.90));
            results.add((int) Math.round(oneRepMax * 0.85));
            results.add((int) Math.round(oneRepMax * 0.80));
            results.add((int) Math.round(oneRepMax * 0.75));
            results.add((int) Math.round(oneRepMax * 0.70));
            results.add((int) Math.round(oneRepMax * 0.65));
            results.add((int) Math.round(oneRepMax * 0.60));
            results.add((int) Math.round(oneRepMax * 0.55));
        }
        return results;
    }

    public List<Integer> calculateLoading(int weight, int barbellWeight, boolean collars) {
        // all amounts are doubled, as barbell has two sides
        int weightToLoad = weight - barbellWeight;
        if (collars) {
            int weightOfCollars = 5;
            weightToLoad -= weightOfCollars;
        }

        List<Integer> results = new ArrayList<>();

        results.add(weightToLoad / 50);
        int rest25 = weightToLoad % 50;
        results.add(rest25 / 40);
        int rest20 = rest25 % 40;
        results.add(rest20 / 30);
        int rest15 = rest20 % 30;
        results.add(rest15 / 20);
        int rest10 = rest15 % 20;
        results.add(rest10 / 10);
        int rest5 = rest10 % 10;
        results.add(rest5 / 5);
        int rest025 = rest5 % 5;
        results.add(rest025 / 4);
        int rest02 = rest025 % 4;
        results.add(rest02 / 3);
        int rest015 = rest02 % 3;
        results.add(rest015 / 2);
        results.add(rest015 % 2);

        return results;
    }


    public String getUnits() {
        return sharedPreferences.getString(context.getString(R.string.settings_units), Units.KG.toString()).toLowerCase();
    }
}
