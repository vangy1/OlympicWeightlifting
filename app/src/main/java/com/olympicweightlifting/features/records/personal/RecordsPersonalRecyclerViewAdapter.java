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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.olympicweightlifting.R;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateFormat.getDateFormat;

/**
 * Created by vangor on 01/02/2018.
 */

public class RecordsPersonalRecyclerViewAdapter extends RecyclerView.Adapter<RecordsPersonalRecyclerViewAdapter.ViewHolder> {
    private List<PersonalRecordData> personalRecordDataList;

    private Context context;
    private DateFormat dateFormat;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.records_layout)
        ConstraintLayout recordsLayout;

        @BindView(R.id.exercise_name)
        TextView exerciseName;
        @BindView(R.id.date_of_record)
        TextView dateOfrecord;
        @BindView(R.id.lifted_weight)
        TextView lifted_weight;
        @BindView(R.id.reps)
        TextView reps;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public RecordsPersonalRecyclerViewAdapter(List<PersonalRecordData> personalRecordDataList, Context context) {
        this.personalRecordDataList = personalRecordDataList;
        this.context = context;
        dateFormat = getDateFormat(context);
    }

    @Override
    public RecordsPersonalRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_personal_records_card, parent, false);
        return new RecordsPersonalRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(RecordsPersonalRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        PersonalRecordData currentPersonalRecordData = personalRecordDataList.get(position);
        viewHolder.recordsLayout.setOnLongClickListener(view -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            CollectionReference personalRecords = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("personal_records");
            personalRecords.document(personalRecordDataList.get(viewHolder.getAdapterPosition()).getDocumentId()).delete();
            return true;
        });

        viewHolder.exerciseName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(21)});
        viewHolder.exerciseName.setText(currentPersonalRecordData.getExercise());

        viewHolder.dateOfrecord.setText(dateFormat.format(currentPersonalRecordData.getDate()));
        viewHolder.lifted_weight.setText(String.format("%s %s", currentPersonalRecordData.getWeightFormatted(), currentPersonalRecordData.getUnits().toLowerCase()));
        viewHolder.reps.setText(context.getResources().getQuantityString(R.plurals.repetitons, currentPersonalRecordData.getReps(), currentPersonalRecordData.getReps()));
    }


    @Override
    public int getItemCount() {
        return personalRecordDataList.size();
    }

}
