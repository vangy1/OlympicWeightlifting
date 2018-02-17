package com.olympicweightlifting.features.programs.list.details;

import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.ProgramDay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vangor on 01/02/2018.
 */

public class ProgramDetailsDaysViewAdapter extends RecyclerView.Adapter<ProgramDetailsDaysViewAdapter.ViewHolder> {
    private final Fragment parentFragment;
    private List<ProgramDay> days;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day_title)
        TextView dayTitle;
        @BindView(R.id.execises_recycler_view)
        RecyclerView exercisesRecyclerView;
        @BindView(R.id.separator)
        View separator;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProgramDetailsDaysViewAdapter(List<ProgramDay> days, Fragment parentFragment) {
        this.days = days;
        this.parentFragment = parentFragment;
    }

    @Override
    public ProgramDetailsDaysViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_custom_workout_day_card, parent, false);
        return new ProgramDetailsDaysViewAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ProgramDetailsDaysViewAdapter.ViewHolder viewHolder, int position) {
        ProgramDay currentProgramDay = days.get(position);
        viewHolder.dayTitle.setText(String.format("Day %s", String.valueOf(position + 1)));
        setupRecyclerView(viewHolder.exercisesRecyclerView, currentProgramDay);
        if (viewHolder.getAdapterPosition() == days.size() - 1) {
            System.out.println("aa");
            viewHolder.separator.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView(RecyclerView daysRecyclerView, ProgramDay currentProgramDay) {
        daysRecyclerView.setHasFixedSize(false);
        daysRecyclerView.setLayoutManager(new LinearLayoutManager(parentFragment.getActivity()));
        daysRecyclerView.setAdapter(new ProgramDetailsExercisesViewAdapter(currentProgramDay.getExercises(), parentFragment));
    }


    @Override
    public int getItemCount() {
        return days.size();
    }

}
