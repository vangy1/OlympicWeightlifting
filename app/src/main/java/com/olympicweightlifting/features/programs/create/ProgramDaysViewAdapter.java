package com.olympicweightlifting.features.programs.create;

import android.content.Context;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramDaysViewAdapter extends RecyclerView.Adapter<ProgramDaysViewAdapter.ViewHolder> {
    private final Context context;
    private List<ProgramDay> days;
    private List<String> userExercises;

    static abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        abstract void bind(List<ProgramDay> days, List<String> exerciseList, ProgramDaysViewAdapter programDaysViewAdapter, Context context);
    }

    static class ShowItemViewHolder extends ViewHolder {
        @BindView(R.id.item_layout)
        ViewGroup itemLayout;
        @BindView(R.id.day_title)
        TextView dayTitle;
        @BindView(R.id.execises_recycler_view)
        RecyclerView exercisesRecyclerView;

        ShowItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramDay> days, List<String> exerciseList, ProgramDaysViewAdapter programDaysViewAdapter, Context context) {
            dayTitle.setText(String.format("Day %s", String.valueOf(getAdapterPosition() + 1)));
            setupRecyclerView(exercisesRecyclerView, days.get(getAdapterPosition()), exerciseList, context);

            itemLayout.setOnLongClickListener(view -> {
                days.remove(getAdapterPosition());
                programDaysViewAdapter.notifyDataSetChanged();
                return true;
            });
        }

        private void setupRecyclerView(RecyclerView daysRecyclerView, ProgramDay currentProgramDay, List<String> exerciseList, Context context) {
            daysRecyclerView.setHasFixedSize(false);
            daysRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            daysRecyclerView.setAdapter(new ProgramExercisesViewAdapter(currentProgramDay.getExercises(), context, exerciseList));
        }
    }

    static class AddItemViewHolder extends ViewHolder {
        @BindView(R.id.add_day_button)
        Button addDayButton;

        AddItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramDay> days, List<String> exerciseList, ProgramDaysViewAdapter programDaysViewAdapter, Context context) {
            addDayButton.setOnClickListener(view -> {
                if (days.size() < 7) {
                    ProgramDay programDay = new ProgramDay(new ArrayList<>());
                    days.add(programDay);
                    programDaysViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "There are only 7 days in a week", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    ProgramDaysViewAdapter(List<ProgramDay> days, List<String> userExercises, Context context) {
        this.days = days;
        this.userExercises = userExercises;
        this.context = context;
    }

    @Override
    public ProgramDaysViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.view_holder_custom_workout_day_add_card) {
            return new ProgramDaysViewAdapter.AddItemViewHolder(view);
        } else {
            return new ProgramDaysViewAdapter.ShowItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ProgramDaysViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bind(days, userExercises, this, context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == days.size())
            return R.layout.view_holder_custom_workout_day_add_card;
        else
            return R.layout.view_holder_custom_workout_day_card;
    }

    @Override
    public int getItemCount() {
        return days.size() + 1;
    }

}
