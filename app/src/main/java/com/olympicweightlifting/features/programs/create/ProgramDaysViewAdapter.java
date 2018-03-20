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
        @BindView(R.id.layout_item)
        ViewGroup layoutItem;
        @BindView(R.id.text_title_day)
        TextView textViewTitleDay;
        @BindView(R.id.recyclerview_exercises)
        RecyclerView recyclerViewExercises;

        ShowItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramDay> days, List<String> exerciseList, ProgramDaysViewAdapter programDaysViewAdapter, Context context) {
            textViewTitleDay.setText(String.format(context.getString(R.string.all_day) + " %s", String.valueOf(getAdapterPosition() + 1)));
            setupRecyclerView(recyclerViewExercises, days.get(getAdapterPosition()), exerciseList, context);

            layoutItem.setOnLongClickListener(view -> {
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
        @BindView(R.id.button_add_day)
        Button buttonAddDay;

        AddItemViewHolder(View view) {
            super(view);
        }

        @Override
        void bind(List<ProgramDay> days, List<String> exerciseList, ProgramDaysViewAdapter programDaysViewAdapter, Context context) {
            buttonAddDay.setOnClickListener(view -> {
                if (days.size() < 7) {
                    ProgramDay programDay = new ProgramDay(new ArrayList<>());
                    days.add(programDay);
                    programDaysViewAdapter.notifyItemInserted(days.size());
                } else {
                    Toast.makeText(context, R.string.programs_error_too_many_days, Toast.LENGTH_SHORT).show();
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
        if (viewType == R.layout.view_holder_programs_add_day) {
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
            return R.layout.view_holder_programs_add_day;
        else
            return R.layout.view_holder_programs_day;
    }

    @Override
    public int getItemCount() {
        return days.size() + 1;
    }

}
