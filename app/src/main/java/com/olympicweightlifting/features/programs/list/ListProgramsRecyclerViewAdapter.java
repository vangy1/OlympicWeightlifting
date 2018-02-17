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

public class ListProgramsRecyclerViewAdapter extends RecyclerView.Adapter<ListProgramsRecyclerViewAdapter.ViewHolder> {
    private Activity activity;

    private List<Program> programList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.records_layout)
        ConstraintLayout recordsLayout;

        @BindView(R.id.program_title)
        TextView programTitle;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public ListProgramsRecyclerViewAdapter(List<Program> programList, Activity actvitiy) {
        this.programList = programList;
        this.activity = actvitiy;

    }

    @Override
    public ListProgramsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_programs_list_card, parent, false);
        return new ListProgramsRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ListProgramsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Program currentProgram = programList.get(position);

        viewHolder.programTitle.setText(currentProgram.getProgramTitle());

        viewHolder.recordsLayout.setOnClickListener(view -> {
            showWorkoutDetails(currentProgram);
        });
    }

    private void showWorkoutDetails(Program program) {
        Bundle bundle = new Bundle();
        bundle.putString("programDetails", new Gson().toJson(program));
        ProgramDetails programDetails = ProgramDetails.newInstance(bundle);
        activity.getFragmentManager().beginTransaction()
                .add(R.id.workout_details_fragment_container, programDetails, "programDetailsFragment")
                .addToBackStack("programDetailsFragment")
                .commit();

        View workoutDetailsFragmentContainer = activity.findViewById(R.id.workout_details_fragment_container);
        workoutDetailsFragmentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

}
