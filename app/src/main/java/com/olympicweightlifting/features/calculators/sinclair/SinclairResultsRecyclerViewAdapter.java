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
    private List<SinclairCalculationDataset> sinclairCalculationDatasets;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.total_text_view)
        TextView totalTextView;
        @BindView(R.id.bodyweight_text_view)
        TextView bodyweightTextView;
        @BindView(R.id.gender_text_view)
        TextView genderTextView;
        @BindView(R.id.sinclairscore_text_view)
        TextView sinclairScoreTextView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public SinclairResultsRecyclerViewAdapter(List<SinclairCalculationDataset> sinclairCalculationDatasets) {
        this.sinclairCalculationDatasets = sinclairCalculationDatasets;
    }

    @Override
    public SinclairResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_sinclair_calculator_result_card, parent, false);
        return new SinclairResultsRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(SinclairResultsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        SinclairCalculationDataset currentSinclairCalculationDataset = sinclairCalculationDatasets.get(position);

        viewHolder.totalTextView.setText(String.valueOf(currentSinclairCalculationDataset.getTotal()));
        viewHolder.bodyweightTextView.setText(String.valueOf(currentSinclairCalculationDataset.getBodyweight()));
        viewHolder.genderTextView.setText(currentSinclairCalculationDataset.getGender().toString());

        Spannable sinclairScoreSpannable = new SpannableString(new DecimalFormat("##.00").format(currentSinclairCalculationDataset.getSinclairScore()));
        sinclairScoreSpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, sinclairScoreSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.sinclairScoreTextView.setText(R.string.sinclair_score_result_text);
        viewHolder.sinclairScoreTextView.append(sinclairScoreSpannable);
    }

    @Override
    public int getItemCount() {
        return sinclairCalculationDatasets.size();
    }

}
