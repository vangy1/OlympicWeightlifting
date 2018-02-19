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
import com.olympicweightlifting.features.calculators.CalculatorsService;
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

import static com.olympicweightlifting.features.calculators.CalculatorsService.RepmaxType;

public class RepmaxCalculatorFragment extends DaggerFragment {
    @BindView(R.id.edittext_weight)
    EditText editTextWeight;
    @BindView(R.id.text_weight_units)
    TextView textViewWeightUnits;
    @BindView(R.id.edittext_sets)
    EditText editTextReps;
    @BindView(R.id.calculation_type_radio_group)
    RadioGroup calculationTypeRadioGroup;
    @BindView(R.id.button_calculate)
    Button buttonCalculate;
    @BindView(R.id.recyclerview_results)
    RecyclerView recyclerViewResults;

    @Inject
    AppDatabase database;
    @Inject
    CalculatorsService calculatorsService;

    private List<RepmaxCalculation> calculationsRepmax = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_calculators_repmax, container, false);
        ButterKnife.bind(this, fragmentView);

        calculatorsService.setUnitsForViews(textViewWeightUnits);
        calculatorsService.setupResultsRecyclerView(recyclerViewResults, new RepmaxResultsRecyclerViewAdapter(calculationsRepmax, calculatorsService));
        calculatorsService.populateRecyclerViewFromDatabase(database.repmaxCalculationDao().get(calculatorsService.HISTORY_MAX), calculationsRepmax, recyclerViewResults);

        editTextReps.setFilters(new InputFilter[]{new EditTextInputFilter(1, 10)});

        buttonCalculate.setOnClickListener(view -> {
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
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        int reps = Integer.parseInt(editTextReps.getText().toString());
        RepmaxType repmaxType = calculationTypeRadioGroup.getCheckedRadioButtonId() == R.id.reps_radio_button ? RepmaxType.REPS : RepmaxType.PERCENTAGE;
        String units = calculatorsService.getUnits();
        List<Integer> repmaxResults = calculatorsService.calculateRepmax(weight, reps, repmaxType);
        return new RepmaxCalculation(repmaxResults, repmaxType.toString(), units);
    }

    private void saveCalculationInDatabase(RepmaxCalculation repmaxCalculation) {
        Completable.fromAction(() -> {
            database.repmaxCalculationDao().insert(repmaxCalculation);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> {
            calculatorsService.insertCalculationIntoRecyclerView(repmaxCalculation, calculationsRepmax, recyclerViewResults);
        }).onErrorComplete().subscribe();
    }
}
