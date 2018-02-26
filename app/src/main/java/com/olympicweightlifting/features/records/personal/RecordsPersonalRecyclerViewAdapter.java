package com.olympicweightlifting.features.records.personal;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.olympicweightlifting.R;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateFormat.getDateFormat;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_RECORDS_PERSONAL;
import static com.olympicweightlifting.utilities.ApplicationConstants.FIREBASE_COLLECTION_USERS;

public class RecordsPersonalRecyclerViewAdapter extends RecyclerView.Adapter<RecordsPersonalRecyclerViewAdapter.ViewHolder> {
    private List<RecordsPersonalData> recordsPersonalDataList;

    private Context context;
    private DateFormat dateFormat;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_records)
        ConstraintLayout layoutRecords;

        @BindView(R.id.text_exercise_name)
        TextView textViewExerciseName;
        @BindView(R.id.text_date_of_record)
        TextView textViewDateOfRecord;
        @BindView(R.id.text_lifted_weight)
        TextView textViewLiftedWeight;
        @BindView(R.id.text_reps)
        TextView textViewReps;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public RecordsPersonalRecyclerViewAdapter(List<RecordsPersonalData> recordsPersonalDataList, Context context) {
        this.recordsPersonalDataList = recordsPersonalDataList;
        this.context = context;
        dateFormat = getDateFormat(context);
    }

    @Override
    public RecordsPersonalRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_personal_record, parent, false);
        return new RecordsPersonalRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(RecordsPersonalRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        RecordsPersonalData currentRecordsPersonalData = recordsPersonalDataList.get(position);
        viewHolder.layoutRecords.setOnLongClickListener(view -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore.getInstance()
                    .collection(FIREBASE_COLLECTION_USERS)
                    .document(currentUser.getUid())
                    .collection(FIREBASE_COLLECTION_RECORDS_PERSONAL)
                    .document(recordsPersonalDataList.get(viewHolder.getAdapterPosition()).getDocumentId()).delete();
            return true;
        });

        viewHolder.textViewExerciseName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(21)});
        viewHolder.textViewExerciseName.setText(currentRecordsPersonalData.getExercise());

        viewHolder.textViewDateOfRecord.setText(dateFormat.format(currentRecordsPersonalData.getDate()));
        viewHolder.textViewLiftedWeight.setText(String.format("%s %s", currentRecordsPersonalData.getWeightFormatted(), currentRecordsPersonalData.getUnits().toLowerCase()));
        viewHolder.textViewReps.setText(context.getResources().getQuantityString(R.plurals.repetitons, currentRecordsPersonalData.getReps(), currentRecordsPersonalData.getReps()));
    }


    @Override
    public int getItemCount() {
        return recordsPersonalDataList.size();
    }

}
