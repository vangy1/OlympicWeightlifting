package com.olympicweightlifting.features.records.world;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateFormat.getDateFormat;

/**
 * Created by vangor on 01/02/2018.
 */

public class RecordsWorldRecyclerViewAdapter extends RecyclerView.Adapter<RecordsWorldRecyclerViewAdapter.ViewHolder> {
    private List<WorldCategoryRecordsData> worldCategoryRecordsDataList;

    private DateFormat dateFormat;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.records_layout)
        ConstraintLayout recordsLayout;

        @BindView(R.id.category_title)
        TextView categoryTitle;

        @BindView(R.id.lifted_weight_snatch)
        TextView liftedWeightSnatch;
        @BindView(R.id.record_holder_snatch)
        TextView recordHolderSnatch;
        @BindView(R.id.date_of_record_snatch)
        TextView dateOfRecordSnatch;
        @BindView(R.id.lifted_weight_caj)
        TextView liftedWeightCaj;
        @BindView(R.id.record_holder_caj)
        TextView recordHolderCaj;
        @BindView(R.id.date_of_record_caj)
        TextView dateOfRecordCaj;
        @BindView(R.id.lifted_weight_total)
        TextView liftedWeightTotal;
        @BindView(R.id.record_holder_total)
        TextView recordHolderTotal;
        @BindView(R.id.date_of_record_total)
        TextView dateOfRecordTotal;


        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public RecordsWorldRecyclerViewAdapter(List<WorldCategoryRecordsData> worldCategoryRecordsDataList, Context context) {
        this.worldCategoryRecordsDataList = worldCategoryRecordsDataList;
        dateFormat = getDateFormat(context);
    }

    @Override
    public RecordsWorldRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_world_records_result_card, parent, false);
        return new RecordsWorldRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(RecordsWorldRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        WorldCategoryRecordsData currentWorldCategoryRecordsData = worldCategoryRecordsDataList.get(position);

        WorldRecordData snatchRecordData = currentWorldCategoryRecordsData.getSnatchRecord();
        WorldRecordData cajRecordData = currentWorldCategoryRecordsData.getCajRecord();
        WorldRecordData totalRecordData = currentWorldCategoryRecordsData.getTotalRecord();

        viewHolder.categoryTitle.setText(String.format("%s - %s", currentWorldCategoryRecordsData.getGender(), currentWorldCategoryRecordsData.getWeightCategory().toUpperCase()));
        setRecords(snatchRecordData, viewHolder.liftedWeightSnatch, viewHolder.recordHolderSnatch, viewHolder.dateOfRecordSnatch);
        setRecords(cajRecordData, viewHolder.liftedWeightCaj, viewHolder.recordHolderCaj, viewHolder.dateOfRecordCaj);
        setRecords(totalRecordData, viewHolder.liftedWeightTotal, viewHolder.recordHolderTotal, viewHolder.dateOfRecordTotal);
    }

    private void setRecords(WorldRecordData recordData, TextView liftedWeight, TextView recordHolder, TextView dateOfRecord) {
        liftedWeight.setText(String.valueOf(recordData.getWeightLiftedInKg() + " kg"));
        recordHolder.setText(recordData.getRecordHolderName());
        dateOfRecord.setText(dateFormat.format(recordData.getDateOfRecord()));
    }


    @Override
    public int getItemCount() {
        return worldCategoryRecordsDataList.size();
    }

}
