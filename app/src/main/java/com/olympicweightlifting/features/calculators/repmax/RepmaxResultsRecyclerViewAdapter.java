package com.olympicweightlifting.features.calculators.repmax;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.calculators.CalculatorsService;
import com.olympicweightlifting.features.calculators.CalculatorsService.RepmaxType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepmaxResultsRecyclerViewAdapter extends RecyclerView.Adapter<RepmaxResultsRecyclerViewAdapter.ViewHolder> {
    private List<RepmaxCalculation> repmaxCalculations;
    private CalculatorsService calculatorsService;


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_results)
        ConstraintLayout layoutResults;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public RepmaxResultsRecyclerViewAdapter(List<RepmaxCalculation> repmaxCalculations, CalculatorsService calculatorsService) {
        this.repmaxCalculations = repmaxCalculations;
        this.calculatorsService = calculatorsService;
    }

    @Override
    public RepmaxResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_calculators_repmax_result, parent, false);
        return new RepmaxResultsRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(RepmaxResultsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        RepmaxCalculation currentRepmaxCalculation = repmaxCalculations.get(position);

        List<TextView> resultsTitleTextViews = new ArrayList<>();
        List<TextView> resultsTextViews = new ArrayList<>();
        calculatorsService.getResultViewsFromLayout(viewHolder.layoutResults, resultsTitleTextViews, resultsTextViews);

        populateResultTitlesTextViews(viewHolder, currentRepmaxCalculation, resultsTitleTextViews);
        populateResultsTextViews(currentRepmaxCalculation, resultsTextViews);
    }

    @Override
    public int getItemCount() {
        return repmaxCalculations.size();
    }

    private void populateResultTitlesTextViews(ViewHolder viewHolder, RepmaxCalculation currentRepmaxCalculation, List<TextView> resultsTitleTextViews) {
        List<String> resultsTitles = getTitlesFromResourcesBasedOnCalculationType(viewHolder.layoutResults.getContext(), currentRepmaxCalculation);
        for (int i = 0; i < resultsTitleTextViews.size(); i++) {
            resultsTitleTextViews.get(i).setText(String.valueOf(resultsTitles.get(i)));
        }
    }

    private void populateResultsTextViews(RepmaxCalculation currentRepmaxCalculation, List<TextView> resultsTextViews) {
        List<Integer> calculationResults = currentRepmaxCalculation.getResults();
        for (int i = 0; i < resultsTextViews.size(); i++) {
            resultsTextViews.get(i).setText(String.valueOf(calculationResults.get(i) + " " + currentRepmaxCalculation.getUnits()));
        }
    }

    private List<String> getTitlesFromResourcesBasedOnCalculationType(Context context, RepmaxCalculation currentRepmaxCalculation) {
        List<String> resultsTitles;
        if (RepmaxType.valueOf(currentRepmaxCalculation.getType()) == RepmaxType.REPS) {
            resultsTitles = Arrays.asList(context.getResources().getStringArray(R.array.calculators_repmax_reps));
        } else {
            resultsTitles = Arrays.asList(context.getResources().getStringArray(R.array.calculators_repmax_percentage));
        }
        return resultsTitles;
    }

}
