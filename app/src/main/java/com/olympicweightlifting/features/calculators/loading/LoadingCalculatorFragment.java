package com.olympicweightlifting.features.calculators.loading;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.calculators.CalculatorService;
import com.olympicweightlifting.utilities.EditTextInputFilter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoadingCalculatorFragment extends DaggerFragment {

    @BindView(R.id.weight_edit_text)
    EditText weightEditText;
    @BindView(R.id.weight_units)
    TextView weightUnitsText;
    @BindView(R.id.barbell_weight_radio_group)
    RadioGroup barbellWeightRadioGroup;
    @BindView(R.id.collars_checkbox)
    CheckBox collarsCheckbox;
    @BindView(R.id.calculate_button)
    Button calculateButton;
    @BindView(R.id.results_recycler_view)
    RecyclerView resultsRecyclerView;

    @Inject
    AppDatabase database;
    @Inject
    CalculatorService calculatorService;

    private List<LoadingCalculation> loadingCalculations = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: migrate to Constraint layout
        View fragmentView = inflater.inflate(R.layout.fragment_loading_calculator, container, false);
        ButterKnife.bind(this, fragmentView);

        calculatorService.setUnitsForViews(weightUnitsText);
        calculatorService.setupResultsRecyclerView(resultsRecyclerView, new LoadingResultsRecyclerViewAdapter(loadingCalculations, calculatorService));
        calculatorService.populateRecyclerViewFromDatabase(database.loadingCalculationDao().get(calculatorService.HISTORY_MAX), loadingCalculations, resultsRecyclerView);

        weightEditText.setFilters(new InputFilter[]{new EditTextInputFilter(1, 9999)});

        calculateButton.setOnClickListener(view -> {
            if (isInputValid()) {
                LoadingCalculation loadingCalculation = calculateLoading();
                saveCalculationInDatabase(loadingCalculation);
                calculatorService.insertCalculationIntoRecyclerView(loadingCalculation, loadingCalculations, resultsRecyclerView);
            } else {
                Toast.makeText(getActivity(), "Fill out all information and make sure that weight is bigger than the weight of barbell and collars!", Toast.LENGTH_SHORT).show();
            }
        });

        return fragmentView;
    }

    private boolean isInputValid() {
        return weightEditText.getText().length() != 0 &&
                barbellWeightRadioGroup.getCheckedRadioButtonId() != -1 &&
                weightToLoadDoesNotExceedWeightOfBar();
    }

    private LoadingCalculation calculateLoading() {
        int weight = Integer.parseInt(weightEditText.getText().toString());
        int barbellWeight = getBarbellWeight();
        boolean collars = collarsCheckbox.isChecked();

        List<Integer> results = calculatorService.calculateLoading(weight, barbellWeight, collars);
        return new LoadingCalculation(weight, barbellWeight, collars, results);
    }

    private void saveCalculationInDatabase(LoadingCalculation loadingCalculation) {
        Completable.fromAction(() -> {
            database.loadingCalculationDao().insert(loadingCalculation);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private boolean weightToLoadDoesNotExceedWeightOfBar() {
        int weightOfCollars = collarsCheckbox.isChecked() ? 5 : 0;
        return Integer.parseInt(weightEditText.getText().toString()) >= getBarbellWeight() + weightOfCollars;
    }

    private int getBarbellWeight() {
        switch (barbellWeightRadioGroup.getCheckedRadioButtonId()) {
            case R.id.men_radio_button:
                return 20;
            case R.id.women_radio_button:
                return 15;
            case R.id.technique_radio_button:
                return 10;
            default:
                return 20;
        }
    }
}
