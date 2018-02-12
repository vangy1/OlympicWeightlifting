package com.olympicweightlifting.features.calculators.sinclair;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import com.olympicweightlifting.utilities.AppLevelConstants.Gender;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SinclairCalculatorFragment extends DaggerFragment {
    @BindView(R.id.total_edit_text)
    EditText totalEditText;
    @BindView(R.id.total_units)
    TextView totalUnitsText;
    @BindView(R.id.bodyweight_edit_text)
    EditText bodyWeightEditText;
    @BindView(R.id.bodyweight_units)
    TextView bodyweightUnitsText;
    @BindView(R.id.gender_radio_group)
    RadioGroup genderRadioGroup;
    @BindView(R.id.calculate_button)
    Button calculateButton;
    @BindView(R.id.results_recycler_view)
    RecyclerView resultsRecyclerView;

    @Inject
    AppDatabase database;
    @Inject
    CalculatorService calculatorService;

    private List<SinclairCalculation> sinclairCalculations = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sinclair_calculator, container, false);
        ButterKnife.bind(this, fragmentView);

        calculatorService.setUnitsForViews(totalUnitsText, bodyweightUnitsText);
        calculatorService.setupResultsRecyclerView(resultsRecyclerView, new SinclairResultsRecyclerViewAdapter(sinclairCalculations));
        calculatorService.populateRecyclerViewFromDatabase(database.sinclairCalculationDao().get(calculatorService.HISTORY_MAX), sinclairCalculations, resultsRecyclerView);

        calculateButton.setOnClickListener(view -> {
            try {
                SinclairCalculation sinclairCalculation = calculateSinclair();
                saveCalculationIntoDatabase(sinclairCalculation);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Fill out all information!", Toast.LENGTH_SHORT).show();
            }

        });

        return fragmentView;
    }

    private SinclairCalculation calculateSinclair() {
        double total = Double.parseDouble(totalEditText.getText().toString());
        double bodyweight = Double.parseDouble(bodyWeightEditText.getText().toString());
        Gender gender = genderRadioGroup.getCheckedRadioButtonId() == R.id.men_radio_button ? Gender.MEN : Gender.WOMEN;
        String units = calculatorService.getUnits();
        double sinclairScore = calculatorService.calculateSinclair(total, bodyweight, gender);
        return new SinclairCalculation(total, bodyweight, gender.toString(), units, sinclairScore);
    }


    private void saveCalculationIntoDatabase(SinclairCalculation sinclairCalculation) {
        Completable.fromAction(() -> {
            database.sinclairCalculationDao().insert(sinclairCalculation);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> {
            calculatorService.insertCalculationIntoRecyclerView(sinclairCalculation, sinclairCalculations, resultsRecyclerView);
        }).onErrorComplete().subscribe();
    }
}