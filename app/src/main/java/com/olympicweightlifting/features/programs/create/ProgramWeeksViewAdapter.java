package com.olympicweightlifting.features.programs.create;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vangor on 01/02/2018.
 */

public class ProgramWeeksViewAdapter extends RecyclerView.Adapter<ProgramWeeksViewAdapter.ViewHolder> {
    private final Context context;
    List<String> userExercises = new ArrayList<>();
    private List<ProgramWeek> weeks;
    private LinearLayoutManager linearLayoutManager;

    static abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        abstract void bind(List<ProgramWeek> weeks, List<String> exerciseList, ProgramWeeksViewAdapter programWeeksViewAdapter, LayoutManager layoutManager, Context context);
    }

    static class ShowItemViewHolder extends ViewHolder {
        @BindView(R.id.item_layout)
        ViewGroup itemLayout;
        @BindView(R.id.week_title)
        TextView weekTitle;
        @BindView(R.id.days_recycler_view)
        RecyclerView daysRecyclerView;

        ShowItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramWeek> weeks, List<String> exerciseList, ProgramWeeksViewAdapter programWeeksViewAdapter, LayoutManager layoutManager, Context context) {
            weekTitle.setText(String.format("Week %s", String.valueOf(getAdapterPosition() + 1)));
            setupRecyclerView(daysRecyclerView, weeks.get(getAdapterPosition()), context, exerciseList);

            itemLayout.setOnLongClickListener(view -> removeWeekAtPosition(weeks, getAdapterPosition(), programWeeksViewAdapter));
        }

        private void setupRecyclerView(RecyclerView daysRecyclerView, ProgramWeek currentProgramWeek, Context context, List<String> exerciseList) {
            daysRecyclerView.setHasFixedSize(false);
            daysRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            daysRecyclerView.setAdapter(new ProgramDaysViewAdapter(currentProgramWeek.getDays(), exerciseList, context));
        }

        private boolean removeWeekAtPosition(List<ProgramWeek> weeks, int position, ProgramWeeksViewAdapter programWeeksViewAdapter) {
            weeks.remove(position);
            programWeeksViewAdapter.notifyDataSetChanged();
            return true;
        }
    }

    static class AddItemViewHolder extends ViewHolder {

        @BindView(R.id.add_week_button)
        Button addWeekButton;

        AddItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramWeek> weeks, List<String> userExercises, ProgramWeeksViewAdapter programWeeksViewAdapter, LayoutManager layoutManager, Context context) {
            addWeekButton.setOnClickListener(view -> {
                addEmptyWeek(weeks, programWeeksViewAdapter, layoutManager, context);
            });
        }

        private void addEmptyWeek(List<ProgramWeek> weeks, ProgramWeeksViewAdapter programWeeksViewAdapter, LayoutManager layoutManager, Context context) {
            ProgramWeek programWeek = new ProgramWeek(new ArrayList<>());
            programWeek.getDays().add(new ProgramDay(new ArrayList<>()));
            weeks.add(programWeek);

            programWeeksViewAdapter.notifyItemInserted(weeks.size());
            scrollToPosition(weeks.size(), layoutManager, context);
        }

        private void scrollToPosition(int position, LayoutManager layoutManager, Context context) {
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                @Override
                protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_END;
                }
            };
            smoothScroller.setTargetPosition(position);
            layoutManager.startSmoothScroll(smoothScroller);
        }


    }

    public ProgramWeeksViewAdapter(Program program, Context context, LinearLayoutManager layout) {
        this.weeks = program.getWeeks();
        this.context = context;
        this.linearLayoutManager = layout;
    }

    @Override
    public ProgramWeeksViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.view_holder_custom_workout_week_add_card) {
            return new ProgramWeeksViewAdapter.AddItemViewHolder(view);
        } else {
            return new ProgramWeeksViewAdapter.ShowItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ProgramWeeksViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bind(weeks, userExercises, this, linearLayoutManager, context);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == weeks.size())
            return R.layout.view_holder_custom_workout_week_add_card;
        else
            return R.layout.view_holder_custom_workout_week_card;
    }

    @Override
    public int getItemCount() {
        return weeks.size() + 1;
    }

    // TODO: find another way to notify nested recycler view (maybe even find alternative to them)
    public void notifyExercisesQueried(List<String> exerciseList) {
        this.userExercises = exerciseList;
        notifyDataSetChanged();
    }

    public void notifyExerciseListModified() {
        notifyDataSetChanged();
    }

}
