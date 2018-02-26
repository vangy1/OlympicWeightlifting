package com.olympicweightlifting.features.records.world;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.records.world.data.WorldCategoryRecordsData;
import com.olympicweightlifting.features.records.world.data.WorldRecordData;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateFormat.getDateFormat;

public class RecordsWorldRecyclerViewAdapter extends RecyclerView.Adapter<RecordsWorldRecyclerViewAdapter.ViewHolder> {
    private List<WorldCategoryRecordsData> worldCategoryRecordsDataList;
    private DateFormat dateFormat;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_records)
        ConstraintLayout layoutRecords;

        @BindView(R.id.text_category_title)
        TextView textViewCategoryTitle;

        @BindView(R.id.text_lifted_weight_snatch)
        TextView textViewLiftedWeightSnatch;
        @BindView(R.id.text_record_holder_snatch)
        TextView textViewRecordHolderSnatch;
        @BindView(R.id.text_date_of_record_snatch)
        TextView textViewDateOfRecordSnatch;
        @BindView(R.id.text_lifted_weight_caj)
        TextView textViewLiftedWeightCaj;
        @BindView(R.id.text_record_holder_caj)
        TextView textViewRecordHolderCaj;
        @BindView(R.id.text_date_of_record_caj)
        TextView textViewDateOfRecordCaj;
        @BindView(R.id.text_lifted_weight_total)
        TextView textViewLiftedWeightTotal;
        @BindView(R.id.text_record_holder_total)
        TextView textViewRecordHolderTotal;
        @BindView(R.id.text_date_of_record_total)
        TextView textViewDateOfRecordTotal;


        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public RecordsWorldRecyclerViewAdapter(List<WorldCategoryRecordsData> worldCategoryRecordsDataList, Context context) {
        this.worldCategoryRecordsDataList = worldCategoryRecordsDataList;
        this.context = context;
        dateFormat = getDateFormat(context);
    }

    @Override
    public RecordsWorldRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_records_world, parent, false);
        return new RecordsWorldRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(RecordsWorldRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        WorldCategoryRecordsData currentWorldCategoryRecordsData = worldCategoryRecordsDataList.get(position);

        WorldRecordData snatchRecordData = currentWorldCategoryRecordsData.getSnatchRecord();
        WorldRecordData cajRecordData = currentWorldCategoryRecordsData.getCajRecord();
        WorldRecordData totalRecordData = currentWorldCategoryRecordsData.getTotalRecord();

        viewHolder.textViewCategoryTitle.setText(String.format("%s - %s", currentWorldCategoryRecordsData.getGender(), currentWorldCategoryRecordsData.getWeightCategory().toUpperCase()));
        setRecords(snatchRecordData, viewHolder.textViewLiftedWeightSnatch, viewHolder.textViewRecordHolderSnatch, viewHolder.textViewDateOfRecordSnatch);
        setRecords(cajRecordData, viewHolder.textViewLiftedWeightCaj, viewHolder.textViewRecordHolderCaj, viewHolder.textViewDateOfRecordCaj);
        setRecords(totalRecordData, viewHolder.textViewLiftedWeightTotal, viewHolder.textViewRecordHolderTotal, viewHolder.textViewDateOfRecordTotal);
    }

    private void setRecords(WorldRecordData recordData, TextView liftedWeight, TextView recordHolder, TextView dateOfRecord) {
        liftedWeight.setText(String.valueOf(String.format("%d " + context.getString(R.string.all_unit_kg), recordData.getWeightLiftedInKg())));
        recordHolder.setText(recordData.getRecordHolderName());
        dateOfRecord.setText(dateFormat.format(recordData.getDateOfRecord()));
    }


    @Override
    public int getItemCount() {
        return worldCategoryRecordsDataList.size();
    }

}
