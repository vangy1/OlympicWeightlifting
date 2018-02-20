package com.olympicweightlifting.features.programs.list;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.Program;
import com.olympicweightlifting.features.programs.list.details.ProgramDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramsListRecyclerViewAdapter extends RecyclerView.Adapter<ProgramsListRecyclerViewAdapter.ViewHolder> {
    public static final String BUNDLE_PROGRAM_DETAILS = "BUNDLE_PROGRAM_DETAILS";

    private Activity activity;

    private List<Program> programList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_records)
        ConstraintLayout layoutRecords;

        @BindView(R.id.text_program_title)
        TextView textViewProgramTitle;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    ProgramsListRecyclerViewAdapter(List<Program> programList, Activity actvitiy) {
        this.programList = programList;
        this.activity = actvitiy;

    }

    @Override
    public ProgramsListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_programs_list, parent, false);
        return new ProgramsListRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ProgramsListRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Program currentProgram = programList.get(position);

        viewHolder.textViewProgramTitle.setText(currentProgram.getProgramTitle());

        viewHolder.layoutRecords.setOnClickListener(view -> {
            showWorkoutDetails(currentProgram);
        });
    }

    private void showWorkoutDetails(Program program) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PROGRAM_DETAILS, new Gson().toJson(program));
        ProgramDetails programDetails = ProgramDetails.newInstance(bundle);
        activity.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_programs, programDetails)
                .addToBackStack(ProgramDetails.TAG)
                .commit();

        View workoutDetailsFragmentContainer = activity.findViewById(R.id.fragment_container_programs);
        workoutDetailsFragmentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

}
