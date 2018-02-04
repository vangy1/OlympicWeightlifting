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
        @BindView(R.id.total_value)
        TextView totalValue;
        @BindView(R.id.bodyweight_value)
        TextView bodyweightValue;
        @BindView(R.id.gender_text_view)
        TextView gendervalue;
        @BindView(R.id.sinclairscore_value)
        TextView sinclairScore;

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
                .inflate(R.layout.view_holder_sinclair_calculator_result_card, parent, false);
        return new SinclairResultsRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(SinclairResultsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        SinclairCalculation currentSinclairCalculation = sinclairCalculations.get(position);

        setCalculationInfoValues(viewHolder, currentSinclairCalculation);
        setResultValue(viewHolder, currentSinclairCalculation);
    }

    private void setCalculationInfoValues(ViewHolder viewHolder, SinclairCalculation currentSinclairCalculation) {
        viewHolder.totalValue.setText(String.format("%s %s", currentSinclairCalculation.getTotalFormatted(), currentSinclairCalculation.getUnits()));
        viewHolder.bodyweightValue.setText(String.format("%s %s", currentSinclairCalculation.getBodyweightFormatted(), currentSinclairCalculation.getUnits()));
        viewHolder.gendervalue.setText(currentSinclairCalculation.getGender());
    }

    private void setResultValue(ViewHolder viewHolder, SinclairCalculation currentSinclairCalculation) {
        Spannable sinclairScoreSpannable = new SpannableString(new DecimalFormat("##.00").format(currentSinclairCalculation.getSinclairScore()));
        sinclairScoreSpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, sinclairScoreSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.sinclairScore.setText(R.string.sinclair_score_result_text);
        viewHolder.sinclairScore.append(sinclairScoreSpannable);
    }

    @Override
    public int getItemCount() {
        return sinclairCalculations.size();
    }

}
