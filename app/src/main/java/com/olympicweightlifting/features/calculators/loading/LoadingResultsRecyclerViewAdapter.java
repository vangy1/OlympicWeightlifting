package com.olympicweightlifting.features.calculators.loading;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.calculators.CalculatorService;
import com.olympicweightlifting.utilities.AppLevelConstants.Units;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by vangor on 04/02/2018.
 */

public class LoadingResultsRecyclerViewAdapter extends RecyclerView.Adapter<LoadingResultsRecyclerViewAdapter.ViewHolder> {
    private List<LoadingCalculation> loadingCalculations;
    private CalculatorService calculatorService;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.weight_value)
        TextView weightValue;
        @BindView(R.id.barbell_value)
        TextView barbellValue;
        @BindView(R.id.collars_value)
        TextView collarsValue;
        @BindView(R.id.results_layout)
        ConstraintLayout resultsLayout;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public LoadingResultsRecyclerViewAdapter(List<LoadingCalculation> loadingCalculations, CalculatorService calculatorService) {
        this.loadingCalculations = loadingCalculations;
        this.calculatorService = calculatorService;
    }

    @Override
    public LoadingResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_loading_calculator_result_card, parent, false);
        return new LoadingResultsRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(LoadingResultsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        LoadingCalculation currentLoadingCalculation = loadingCalculations.get(position);

        setCalculationInfoValues(viewHolder, currentLoadingCalculation);
        setResultValues(viewHolder, currentLoadingCalculation);
    }

    private void setCalculationInfoValues(ViewHolder viewHolder, LoadingCalculation currentLoadingCalculation) {
        viewHolder.weightValue.setText(String.format("%s %s", currentLoadingCalculation.getWeight(), Units.KG.toString().toLowerCase()));
        viewHolder.barbellValue.setText(String.format("%s %s", currentLoadingCalculation.getBarbell(), Units.KG.toString().toLowerCase()));
        viewHolder.collarsValue.setText(currentLoadingCalculation.hasCollars() ? "Yes" : "No");
    }

    private void setResultValues(ViewHolder viewHolder, LoadingCalculation currentLoadingCalculation) {
        List<TextView> resultsTitleTextViews = new ArrayList<>();
        List<TextView> resultsTextViews = new ArrayList<>();

        calculatorService.getResultViewsFromLayout(viewHolder.resultsLayout, resultsTitleTextViews, resultsTextViews);
        populateResultTitlesTextViews(viewHolder.resultsLayout.getContext(), currentLoadingCalculation, resultsTitleTextViews);
        populateResultsTextViews(currentLoadingCalculation, resultsTextViews);
    }

    private void populateResultTitlesTextViews(Context context, LoadingCalculation currentRepmaxCalculation, List<TextView> resultsTitleTextViews) {

        List<String> loadingWeights = Arrays.asList(context.getResources().getStringArray(R.array.calculators_loading_weights));
        List<String> loadingWeightColors = Arrays.asList(context.getResources().getStringArray(R.array.calculators_loading_weight_colors));

        for (int i = 0; i < resultsTitleTextViews.size(); i++) {
            TextView resultTitle = resultsTitleTextViews.get(i);
            resultTitle.setText(loadingWeights.get(i));
            resultTitle.setTextColor(Color.parseColor(loadingWeightColors.get(i)));
        }
    }

    private void populateResultsTextViews(LoadingCalculation currentRepmaxCalculation, List<TextView> resultsTextViews) {
        List<Integer> calculationResults = currentRepmaxCalculation.getResults();
        for (int i = 0; i < resultsTextViews.size(); i++) {
            resultsTextViews.get(i).setText(String.valueOf(calculationResults.get(i)));
        }
    }

    @Override
    public int getItemCount() {
        return loadingCalculations.size();
    }
}
