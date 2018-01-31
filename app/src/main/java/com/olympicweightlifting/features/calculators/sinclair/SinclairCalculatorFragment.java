package com.olympicweightlifting.features.calculators.sinclair;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mikepenz.itemanimators.SlideRightAlphaAnimator;
import com.olympicweightlifting.AppDatabase;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.calculators.Calculator;
import com.olympicweightlifting.features.calculators.Calculator.Gender;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;


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

    private List<SinclairCalculation> sinclairCalculations = new ArrayList<>();
    private RecyclerView.Adapter resultsRecyclerViewAdapter;
    private AppDatabase database;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, getString(R.string.database_name)).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sinclair_calculator, container, false);
        ButterKnife.bind(this, fragmentView);
        setupRecyclerView();

        populateRecyclerView();


        calculateButton.setOnClickListener(view -> {
            SinclairCalculation sinclairCalculation = calculateSinclair();

            saveCalculationInDatabase(sinclairCalculation);
            insertCalculationIntoRecyclerView(sinclairCalculation);
        });

        return fragmentView;
    }

    private void setupRecyclerView() {
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsRecyclerViewAdapter = new SinclairResultsRecyclerViewAdapter(sinclairCalculations);
        resultsRecyclerView.setAdapter(resultsRecyclerViewAdapter);
        resultsRecyclerView.setItemAnimator(new SlideRightAlphaAnimator());
    }

    private void populateRecyclerView() {
        database.sinclairCalculationDao().get(HISTORY_MAX).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((calculations) -> {
            sinclairCalculations.addAll(calculations);
            resultsRecyclerViewAdapter.notifyDataSetChanged();
        });
    }

    private SinclairCalculation calculateSinclair() {
        double total = Double.parseDouble(this.total.getText().toString());
        double bodyweight = Double.parseDouble(bodyWeightEditText.getText().toString());
        Gender gender = genderRadioGroup.getCheckedRadioButtonId() == R.id.male_radio_button ? Gender.MALE : Gender.FEMALE;
        double sinclairScore = calculator.calculateSinclair(total, bodyweight, gender);
        return new SinclairCalculation(total, bodyweight, gender.toString(), sinclairScore);
    }

    private void saveCalculationInDatabase(SinclairCalculation sinclairCalculation) {
        Completable.fromAction(() -> {
            database.sinclairCalculationDao().insert(sinclairCalculation);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private void insertCalculationIntoRecyclerView(SinclairCalculation sinclairCalculation) {
        if (sinclairCalculations.size() >= HISTORY_MAX) {
            sinclairCalculations.remove(sinclairCalculations.size() - 1);
            resultsRecyclerViewAdapter.notifyItemRangeRemoved(sinclairCalculations.size(), 1);
        }
        sinclairCalculations.add(0, sinclairCalculation);
        resultsRecyclerViewAdapter.notifyItemInserted(0);
        resultsRecyclerView.scrollToPosition(0);
    }
}