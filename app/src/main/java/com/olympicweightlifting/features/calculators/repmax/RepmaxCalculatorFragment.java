package com.olympicweightlifting.features.calculators.repmax;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import dagger.android.support.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.olympicweightlifting.features.calculators.CalculatorService.RepmaxType;

public class RepmaxCalculatorFragment extends DaggerFragment {

    @BindView(R.id.weight_edit_text)
    EditText weightEditText;
    @BindView(R.id.weight_units)
    TextView weightUnitsText;
    @BindView(R.id.sets_edit_text)
    EditText repsEditText;
    @BindView(R.id.calculation_type_radio_group)
    RadioGroup calculationTypeRadioGroup;
    @BindView(R.id.calculate_button)
    Button calculateButton;
    @BindView(R.id.results_recycler_view)
    RecyclerView resultsRecyclerView;

    @Inject
    AppDatabase database;
    @Inject
    CalculatorService calculatorService;

    private List<RepmaxCalculation> repmaxCalculations = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_repmax_calculator, container, false);
        ButterKnife.bind(this, fragmentView);

        calculatorService.setUnitsForViews(weightUnitsText);
        calculatorService.setupResultsRecyclerView(resultsRecyclerView, new RepmaxResultsRecyclerViewAdapter(repmaxCalculations, calculatorService));
        calculatorService.populateRecyclerViewFromDatabase(database.repmaxCalculationDao().get(calculatorService.HISTORY_MAX), repmaxCalculations, resultsRecyclerView);

        repsEditText.setFilters(new InputFilter[]{new EditTextInputFilter(1, 10)});

        calculateButton.setOnClickListener(view -> {
            try {
                RepmaxCalculation repmaxCalculation = calculateRepmax();
                saveCalculationInDatabase(repmaxCalculation);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Fill out all information!", Toast.LENGTH_SHORT).show();
            }
        });

        return fragmentView;
    }
    private RepmaxCalculation calculateRepmax() {
        double weight = Double.parseDouble(weightEditText.getText().toString());
        int reps = Integer.parseInt(repsEditText.getText().toString());
        RepmaxType repmaxType = calculationTypeRadioGroup.getCheckedRadioButtonId() == R.id.reps_radio_button ? RepmaxType.REPS : RepmaxType.PERCENTAGE;
        String units = calculatorService.getUnits();
        List<Integer> repmaxResults = calculatorService.calculateRepmax(weight, reps, repmaxType);
        return new RepmaxCalculation(repmaxResults, repmaxType.toString(), units);
    }

    private void saveCalculationInDatabase(RepmaxCalculation repmaxCalculation) {
        Completable.fromAction(() -> {
            database.repmaxCalculationDao().insert(repmaxCalculation);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> {
            calculatorService.insertCalculationIntoRecyclerView(repmaxCalculation, repmaxCalculations, resultsRecyclerView);
        }).onErrorComplete().subscribe();
    }
}
