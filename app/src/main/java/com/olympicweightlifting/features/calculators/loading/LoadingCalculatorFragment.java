package com.olympicweightlifting.features.calculators.loading;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.olympicweightlifting.App;
import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.calculators.CalculatorsService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoadingCalculatorFragment extends DaggerFragment {

    @BindView(R.id.edittext_weight)
    EditText editTextWeight;
    @BindView(R.id.text_weight_units)
    TextView textViewWeightUnits;
    @BindView(R.id.radiogroup_barbell_weight)
    RadioGroup radioGroupBarbellWeight;
    @BindView(R.id.checkbox_collars)
    CheckBox checkboxCollars;
    @BindView(R.id.button_calculate)
    Button buttonCalculate;
    @BindView(R.id.recyclerview_results)
    RecyclerView recyclerViewResults;

    @Inject
    AppDatabase database;
    @Inject
    CalculatorsService calculatorsService;

    private List<LoadingCalculation> loadingCalculations = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_calculators_loading, container, false);
        ButterKnife.bind(this, fragmentView);

        calculatorsService.setupResultsRecyclerView(recyclerViewResults, new LoadingResultsRecyclerViewAdapter(loadingCalculations, calculatorsService));
        calculatorsService.populateRecyclerViewFromDatabase(database.loadingCalculationDao().get(calculatorsService.HISTORY_MAX), loadingCalculations, recyclerViewResults);

        buttonCalculate.setOnClickListener(view -> {
            try {
                LoadingCalculation loadingCalculation = calculateLoading();
                saveCalculationInDatabase(loadingCalculation);
                ((App) getActivity().getApplication()).getAnalyticsTracker().sendEvent("Calculator Activity", "Calculate loading");
            } catch (WeightIsSmallerThanTheBarException e) {
                Toast.makeText(getActivity(), R.string.calculators_loading_error_weight_smaller_than_bar, Toast.LENGTH_SHORT).show();
            } catch (Exception exception) {
                Toast.makeText(getActivity(), R.string.all_insufficient_input, Toast.LENGTH_SHORT).show();
            }
        });

        return fragmentView;
    }

    private LoadingCalculation calculateLoading() throws WeightIsSmallerThanTheBarException {
        int weight = Integer.parseInt(editTextWeight.getText().toString());
        int barbellWeight = getBarbellWeight();
        boolean collars = checkboxCollars.isChecked();

        int collarsWeight = collars ? 5 : 0;
        if (weight <= barbellWeight + collarsWeight) {
            throw new WeightIsSmallerThanTheBarException();
        }

        List<Integer> results = calculatorsService.calculateLoading(weight, barbellWeight, collars);
        return new LoadingCalculation(weight, barbellWeight, collars, results);
    }

    private void saveCalculationInDatabase(LoadingCalculation loadingCalculation) {
        Completable.fromAction(() -> database.loadingCalculationDao().insert(loadingCalculation)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() ->
                calculatorsService.insertCalculationIntoRecyclerView(loadingCalculation, loadingCalculations, recyclerViewResults)).onErrorComplete().subscribe();
    }

    private int getBarbellWeight() {
        switch (radioGroupBarbellWeight.getCheckedRadioButtonId()) {
            case R.id.radiobutton_men:
                return 20;
            case R.id.radiobutton_women:
                return 15;
            case R.id.radiobutton_technique:
                return 10;
            default:
                return 20;
        }
    }

}
