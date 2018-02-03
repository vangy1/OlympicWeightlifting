package com.olympicweightlifting.features.calculators.repmax;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.data.local.AppDatabase;
import com.olympicweightlifting.features.calculators.CalculatorService;
import com.olympicweightlifting.mainpage.SettingsDialog;
import com.olympicweightlifting.utilities.EditTextInputFilter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.olympicweightlifting.features.calculators.CalculatorService.RepmaxType;

public class RepmaxCalculatorFragment extends DaggerFragment {

    @BindView(R.id.weight_edit_text)
    EditText weightEditText;
    @BindView(R.id.weight_units)
    TextView weightUnitsText;
    @BindView(R.id.reps_edit_text)
    EditText repsEditText;
    @BindView(R.id.calculate_button)
    Button calculateButton;
    @BindView(R.id.calculation_type_radio_group)
    RadioGroup calculationTypeRadioGroup;
    @BindView(R.id.results_recycler_view)
    RecyclerView resultsRecyclerView;

    @Inject
    @Named("settings")
    SharedPreferences settingsSharedPreferences;
    @Inject
    AppDatabase database;
    @Inject
    CalculatorService calculatorService;

    private List<RepmaxCalculation> repmaxCalculations = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_repmax_calculator, container, false);
        ButterKnife.bind(this, fragmentView);

        calculatorService.setUnitsForViews(weightUnitsText);
        calculatorService.setupResultsRecyclerView(resultsRecyclerView, new RepmaxResultsRecyclerViewAdapter(repmaxCalculations));
        calculatorService.populateRecyclerViewFromDatabase(database.repmaxCalculation().get(calculatorService.HISTORY_MAX), repmaxCalculations, resultsRecyclerView);

        repsEditText.setFilters(new InputFilter[]{new EditTextInputFilter(1, 10)});

        calculateButton.setOnClickListener(view -> {
            RepmaxCalculation repmaxCalculation = calculateRepmax();

            saveCalculationInDatabase(repmaxCalculation);
            calculatorService.insertCalculationIntoRecyclerView(repmaxCalculation, repmaxCalculations, resultsRecyclerView);
        });


        return fragmentView;

    }

    private RepmaxCalculation calculateRepmax() {
        double weight = Double.parseDouble(weightEditText.getText().toString());
        int reps = Integer.parseInt(repsEditText.getText().toString());
        RepmaxType repmaxType = calculationTypeRadioGroup.getCheckedRadioButtonId() == R.id.reps_radio_button ? RepmaxType.REPS : RepmaxType.PERCENTAGE;
        String units = settingsSharedPreferences.getString(getActivity().getString(R.string.settings_units), SettingsDialog.Units.KG.toString()).toLowerCase();
        List<Integer> repmaxResults = calculatorService.calculateRepmax(weight, reps, repmaxType);
        return new RepmaxCalculation(repmaxResults, repmaxType.toString(), units);
    }

    private void saveCalculationInDatabase(RepmaxCalculation repmaxCalculation) {
        Completable.fromAction(() -> {
            database.repmaxCalculation().insert(repmaxCalculation);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
