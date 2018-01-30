package com.olympicweightlifting.calculators.sinclair;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mikepenz.itemanimators.SlideRightAlphaAnimator;
import com.olympicweightlifting.R;
import com.olympicweightlifting.calculators.Calculator;
import com.olympicweightlifting.calculators.Calculator.Gender;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerFragment;


public class SinclairCalculatorFragment extends DaggerFragment {
    private final int HISTORY_MAX = 10;

    @BindView(R.id.total_edit_text)
    EditText total;
    @BindView(R.id.bodyweight_edit_text)
    EditText bodyWeightEditText;
    @BindView(R.id.gender_radio_group)
    RadioGroup genderRadioGroup;
    @BindView(R.id.calculate_button)
    Button calculateButton;
    @BindView(R.id.results_recycler_view)
    RecyclerView resultsRecyclerView;

    @Inject
    Calculator calculator;

    private List<SinclairCalculationDataset> sinclairCalculationDatasets = new ArrayList<>();
    private RecyclerView.Adapter resultsRecyclerViewAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sinclair_calculator, container, false);
        ButterKnife.bind(this, fragmentView);
        setupRecyclerView();


        calculateButton.setOnClickListener(view -> {
            double total = Double.parseDouble(this.total.getText().toString());
            double bodyweight = Double.parseDouble(bodyWeightEditText.getText().toString());
            Gender gender = genderRadioGroup.getCheckedRadioButtonId() == R.id.male_radio_button ? Gender.MALE : Gender.FEMALE;
            double sinclairScore = calculator.calculateSinclair(total, bodyweight, gender);

            if(sinclairCalculationDatasets.size() >= HISTORY_MAX){
                sinclairCalculationDatasets.remove(sinclairCalculationDatasets.size() - 1);
                resultsRecyclerViewAdapter.notifyItemRangeRemoved(sinclairCalculationDatasets.size(), 1);

            }
            sinclairCalculationDatasets.add(0,new SinclairCalculationDataset(total,bodyweight,gender,sinclairScore));
            resultsRecyclerViewAdapter.notifyItemInserted(0);
            resultsRecyclerView.scrollToPosition(0);

        });

        return fragmentView;
    }

    private void setupRecyclerView() {
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsRecyclerViewAdapter = new SinclairResultsRecyclerViewAdapter(sinclairCalculationDatasets);
        resultsRecyclerView.setAdapter(resultsRecyclerViewAdapter);
        resultsRecyclerView.setItemAnimator(new SlideRightAlphaAnimator());
    }


}