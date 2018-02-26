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
import com.olympicweightlifting.features.calculators.CalculatorsService;
import com.olympicweightlifting.utilities.ApplicationConstants.Gender;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SinclairCalculatorFragment extends DaggerFragment {
    @BindView(R.id.edittext_total)
    EditText editTextTotal;
    @BindView(R.id.text_total_units)
    TextView textTotalUnits;
    @BindView(R.id.edittext_bodyweight)
    EditText editTextBodyweight;
    @BindView(R.id.text_bodyweight_units)
    TextView textBodyweightUnits;
    @BindView(R.id.radiogroup_gender)
    RadioGroup radioGroupGender;
    @BindView(R.id.button_calculate)
    Button buttonCalculate;
    @BindView(R.id.recyclerview_results)
    RecyclerView recyclerViewResults;

    @Inject
    AppDatabase database;
    @Inject
    CalculatorsService calculatorsService;

    private List<SinclairCalculation> sinclairCalculations = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_calculators_sinclair, container, false);
        ButterKnife.bind(this, fragmentView);

        calculatorsService.setUnitsForViews(textTotalUnits, textBodyweightUnits);
        calculatorsService.setupResultsRecyclerView(recyclerViewResults, new SinclairResultsRecyclerViewAdapter(sinclairCalculations));
        calculatorsService.populateRecyclerViewFromDatabase(database.sinclairCalculationDao().get(calculatorsService.HISTORY_MAX), sinclairCalculations, recyclerViewResults);

        buttonCalculate.setOnClickListener(view -> {
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
        double total = Double.parseDouble(editTextTotal.getText().toString());
        double bodyweight = Double.parseDouble(editTextBodyweight.getText().toString());
        Gender gender = radioGroupGender.getCheckedRadioButtonId() == R.id.radiobutton_men ? Gender.MEN : Gender.WOMEN;
        String units = calculatorsService.getUnits();
        double sinclairScore = calculatorsService.calculateSinclair(total, bodyweight, gender);
        return new SinclairCalculation(total, bodyweight, gender.toString(), units, sinclairScore);
    }


    private void saveCalculationIntoDatabase(SinclairCalculation sinclairCalculation) {
        Completable.fromAction(() -> database.sinclairCalculationDao().insert(sinclairCalculation)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() ->
                calculatorsService.insertCalculationIntoRecyclerView(sinclairCalculation, sinclairCalculations, recyclerViewResults)).onErrorComplete().subscribe();
    }
}