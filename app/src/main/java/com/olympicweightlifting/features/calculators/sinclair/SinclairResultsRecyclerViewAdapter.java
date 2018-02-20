package com.olympicweightlifting.features.calculators.sinclair;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SinclairResultsRecyclerViewAdapter extends RecyclerView.Adapter<SinclairResultsRecyclerViewAdapter.ViewHolder> {
    private List<SinclairCalculation> sinclairCalculations;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_total_value)
        TextView textViewTotalValue;
        @BindView(R.id.text_bodyweight_value)
        TextView textViewBodyweightValue;
        @BindView(R.id.text_gender)
        TextView textViewGender;
        @BindView(R.id.text_sinclairscore)
        TextView textViewSinclairScore;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public SinclairResultsRecyclerViewAdapter(List<SinclairCalculation> sinclairCalculations) {
        this.sinclairCalculations = sinclairCalculations;
    }

    @Override
    public SinclairResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_calculators_sinclair_result, parent, false);
        return new SinclairResultsRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(SinclairResultsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        SinclairCalculation currentSinclairCalculation = sinclairCalculations.get(position);

        setCalculationInfoValues(viewHolder, currentSinclairCalculation);
        setResultValue(viewHolder, currentSinclairCalculation);
    }

    private void setCalculationInfoValues(ViewHolder viewHolder, SinclairCalculation currentSinclairCalculation) {
        viewHolder.textViewTotalValue.setText(String.format("%s %s", currentSinclairCalculation.getTotalFormatted(), currentSinclairCalculation.getUnits()));
        viewHolder.textViewBodyweightValue.setText(String.format("%s %s", currentSinclairCalculation.getBodyweightFormatted(), currentSinclairCalculation.getUnits()));
        viewHolder.textViewGender.setText(currentSinclairCalculation.getGender());
    }

    private void setResultValue(ViewHolder viewHolder, SinclairCalculation currentSinclairCalculation) {
        Spannable sinclairScoreSpannable = new SpannableString(new DecimalFormat("##.00").format(currentSinclairCalculation.getSinclairScore()));
        sinclairScoreSpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, sinclairScoreSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textViewSinclairScore.setText(R.string.calculators_sinclair_result);
        viewHolder.textViewSinclairScore.append(sinclairScoreSpannable);
    }

    @Override
    public int getItemCount() {
        return sinclairCalculations.size();
    }

}
