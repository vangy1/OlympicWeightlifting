package com.olympicweightlifting.features.programs.create;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.Program;
import com.olympicweightlifting.features.programs.data.ProgramDay;
import com.olympicweightlifting.features.programs.data.ProgramWeek;

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

public class ProgramWeeksViewAdapter extends RecyclerView.Adapter<ProgramWeeksViewAdapter.ViewHolder> {
    private final Fragment parentFragment;
    List<String> exerciseList = new ArrayList<>();
    private List<ProgramWeek> weeks;
    private LinearLayoutManager linearLayoutManager;

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.week_title)
        TextView weekTitle;
        @Nullable
        @BindView(R.id.days_recycler_view)
        RecyclerView daysRecyclerView;
        @Nullable
        @BindView(R.id.add_week_button)
        Button addWeekButton;

        View view;
        int viewType;

        ViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
            this.viewType = viewType;
        }
    }

    public ProgramWeeksViewAdapter(Program program, Fragment parentFragment, LinearLayoutManager layout) {
        this.weeks = program.getWeeks();
        this.parentFragment = parentFragment;
        this.linearLayoutManager = layout;
    }

    @Override
    public ProgramWeeksViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOLDER_ITEM_NORMAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_holder_custom_workout_week_card, parent, false);
            return new ProgramWeeksViewAdapter.ViewHolder(view, VIEW_HOLDER_ITEM_NORMAL);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_holder_custom_workout_week_add_card, parent, false);
            return new ProgramWeeksViewAdapter.ViewHolder(view, VIEW_HOLDER_ITEM_LAST);
        }

    }

    @Override
    public void onBindViewHolder(ProgramWeeksViewAdapter.ViewHolder viewHolder, int position) {
        if (viewHolder.viewType == VIEW_HOLDER_ITEM_NORMAL) {
            ProgramWeek currentProgramWeek = weeks.get(position);
            viewHolder.weekTitle.setText(String.format("Week %s", String.valueOf(position + 1)));
            setupRecyclerView(viewHolder.daysRecyclerView, currentProgramWeek);

            viewHolder.view.setOnLongClickListener(view -> {
                weeks.remove(viewHolder.getAdapterPosition());
                notifyDataSetChanged();
                return true;
            });
        } else {
            viewHolder.addWeekButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProgramDay programDay = new ProgramDay(new ArrayList<>(Arrays.asList()));
                    ProgramWeek programWeek = new ProgramWeek(new ArrayList<>(Arrays.asList(programDay)));


                    weeks.add(programWeek);
                    notifyItemInserted(weeks.size());

                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(parentFragment.getActivity()) {
                        @Override
                        protected int getVerticalSnapPreference() {
                            return LinearSmoothScroller.SNAP_TO_END;
                        }
                    };
                    smoothScroller.setTargetPosition(weeks.size());
                    linearLayoutManager.startSmoothScroll(smoothScroller);
                }
            });
        }
    }

    private void setupRecyclerView(RecyclerView daysRecyclerView, ProgramWeek currentProgramWeek) {
        daysRecyclerView.setHasFixedSize(false);
        LinearLayoutManager layout = new LinearLayoutManager(parentFragment.getActivity());
        daysRecyclerView.setLayoutManager(layout);
        daysRecyclerView.setAdapter(new ProgramDaysViewAdapter(currentProgramWeek.getDays(), parentFragment, exerciseList));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == weeks.size())
            return VIEW_HOLDER_ITEM_LAST;
        else
            return VIEW_HOLDER_ITEM_NORMAL;

    }

    @Override
    public int getItemCount() {
        return weeks.size() + 1;
    }

    // TODO: find another way to notify nested recycler view (maybe even find alternative to them)
    public void notifyExercisesQueried(List<String> exerciseList) {
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    public void notifyExerciseListModified() {
        notifyDataSetChanged();
    }

}
