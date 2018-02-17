package com.olympicweightlifting.features.programs.create;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.ProgramDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.olympicweightlifting.utilities.AppLevelConstants.VIEW_HOLDER_ITEM_LAST;
import static com.olympicweightlifting.utilities.AppLevelConstants.VIEW_HOLDER_ITEM_NORMAL;

/**
 * Created by vangor on 01/02/2018.
 */

public class ProgramDaysViewAdapter extends RecyclerView.Adapter<ProgramDaysViewAdapter.ViewHolder> {
    private final Fragment parentFragment;
    private List<String> exerciseList;
    private List<ProgramDay> days;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.day_title)
        TextView dayTitle;
        @Nullable
        @BindView(R.id.execises_recycler_view)
        RecyclerView exercisesRecyclerView;
        @Nullable
        @BindView(R.id.add_day_button)
        Button addDayButton;


        int viewType;
        View view;

        ViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
            this.viewType = viewType;
            this.view = view;
        }
    }

    public ProgramDaysViewAdapter(List<ProgramDay> days, Fragment parentFragment, List<String> exerciseList) {
        this.days = days;
        this.parentFragment = parentFragment;
        this.exerciseList = exerciseList;
    }

    @Override
    public ProgramDaysViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOLDER_ITEM_NORMAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_holder_custom_workout_day_card, parent, false);
            return new ProgramDaysViewAdapter.ViewHolder(view, VIEW_HOLDER_ITEM_NORMAL);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_holder_custom_workout_day_add_card, parent, false);
            return new ProgramDaysViewAdapter.ViewHolder(view, VIEW_HOLDER_ITEM_LAST);
        }

    }

    @Override
    public void onBindViewHolder(ProgramDaysViewAdapter.ViewHolder viewHolder, int position) {
        if (viewHolder.viewType == VIEW_HOLDER_ITEM_NORMAL) {
            ProgramDay currentProgramDay = days.get(position);
            viewHolder.dayTitle.setText(String.format("Day %s", String.valueOf(position + 1)));
            setupRecyclerView(viewHolder.exercisesRecyclerView, currentProgramDay);
            viewHolder.view.setOnLongClickListener(view -> {
                days.remove(viewHolder.getAdapterPosition());
                notifyDataSetChanged();
                return true;
            });
        } else {
            viewHolder.addDayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (days.size() < 7) {
                        ProgramDay programDay = new ProgramDay(new ArrayList<>(Arrays.asList()));
                        days.add(programDay);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(parentFragment.getActivity(), "There are only 7 days in a week", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void setupRecyclerView(RecyclerView daysRecyclerView, ProgramDay currentProgramDay) {
        daysRecyclerView.setHasFixedSize(false);
        daysRecyclerView.setLayoutManager(new LinearLayoutManager(parentFragment.getActivity()));
        daysRecyclerView.setAdapter(new ProgramExercisesViewAdapter(currentProgramDay.getExercises(), parentFragment, exerciseList));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == days.size())
            return VIEW_HOLDER_ITEM_LAST;
        else
            return VIEW_HOLDER_ITEM_NORMAL;
    }

    @Override
    public int getItemCount() {
        return days.size() + 1;
    }

}
