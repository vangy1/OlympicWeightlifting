package com.olympicweightlifting.features.calculators.repmax;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.calculators.CalculatorService.RepmaxType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vangor on 01/02/2018.
 */

public class RepmaxResultsRecyclerViewAdapter extends RecyclerView.Adapter<RepmaxResultsRecyclerViewAdapter.ViewHolder> {
    private List<RepmaxCalculation> repmaxCalculations;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.results_layout)
        ConstraintLayout resultsLayout;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public RepmaxResultsRecyclerViewAdapter(List<RepmaxCalculation> repmaxCalculations) {
        this.repmaxCalculations = repmaxCalculations;
    }

    @Override
    public RepmaxResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_repmax_calculator_result_card, parent, false);
        return new RepmaxResultsRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(RepmaxResultsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        RepmaxCalculation currentRepmaxCalculation = repmaxCalculations.get(position);

        List<TextView> resultsTitleTextViews = new ArrayList<>();
        List<TextView> resultsTextViews = new ArrayList<>();
        getViewsFromLayout(viewHolder, resultsTitleTextViews, resultsTextViews);

        populateResultTitlesTextViews(viewHolder, currentRepmaxCalculation, resultsTitleTextViews);
        populateResultsTextViews(currentRepmaxCalculation, resultsTextViews);
    }

    @Override
    public int getItemCount() {
        return repmaxCalculations.size();
    }

    private void getViewsFromLayout(ViewHolder viewHolder, List<TextView> resultsTitleTextViews, List<TextView> resultsTextViews) {
        int childCount = viewHolder.resultsLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewHolder.resultsLayout.getChildAt(i);
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

    private void populateResultTitlesTextViews(ViewHolder viewHolder, RepmaxCalculation currentRepmaxCalculation, List<TextView> resultsTitleTextViews) {
        List<String> resultsTitles = getTitlesFromResourcesBasedOnCalculationType(viewHolder, currentRepmaxCalculation);
        for (int i = 0; i < resultsTitleTextViews.size(); i++) {
            resultsTitleTextViews.get(i).setText(String.valueOf(resultsTitles.get(i)));
        }
    }

    private List<String> getTitlesFromResourcesBasedOnCalculationType(ViewHolder viewHolder, RepmaxCalculation currentRepmaxCalculation) {
        List<String> resultsTitles;
        if (RepmaxType.valueOf(currentRepmaxCalculation.getType()) == RepmaxType.REPS) {
            resultsTitles = Arrays.asList(viewHolder.resultsLayout.getContext().getResources().getStringArray(R.array.calculators_repmax_reps));
        } else {
            resultsTitles = Arrays.asList(viewHolder.resultsLayout.getContext().getResources().getStringArray(R.array.calculators_repmax_percentage));
        }
        return resultsTitles;
    }

    private void populateResultsTextViews(RepmaxCalculation currentRepmaxCalculation, List<TextView> resultsTextViews) {
        List<Integer> calculationResults = currentRepmaxCalculation.getResults();
        for (int i = 0; i < resultsTextViews.size(); i++) {
            resultsTextViews.get(i).setText(String.valueOf(calculationResults.get(i) + " " + currentRepmaxCalculation.getUnits()));
        }
    }


}
