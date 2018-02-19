package com.olympicweightlifting.features.programs.list.details;

import android.content.Context;
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


public class ProgramDetailsDaysViewAdapter extends RecyclerView.Adapter<ProgramDetailsDaysViewAdapter.ViewHolder> {
    private final Context context;
    private List<ProgramDay> days;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title_day)
        TextView textViewDayTitle;
        @BindView(R.id.recyclerview_exercises)
        RecyclerView recyclerViewExercises;
        @BindView(R.id.separator)
        View viewSeparator;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProgramDetailsDaysViewAdapter(List<ProgramDay> days, Context context) {
        this.days = days;
        this.context = context;
    }

    @Override
    public ProgramDetailsDaysViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_programs_day, parent, false);
        return new ProgramDetailsDaysViewAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ProgramDetailsDaysViewAdapter.ViewHolder viewHolder, int position) {
        ProgramDay currentProgramDay = days.get(position);
        viewHolder.textViewDayTitle.setText(String.format("Day %s", String.valueOf(position + 1)));
        setupRecyclerView(viewHolder.recyclerViewExercises, currentProgramDay);
        if (viewHolder.getAdapterPosition() == days.size() - 1) {
            viewHolder.viewSeparator.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView(RecyclerView daysRecyclerView, ProgramDay currentProgramDay) {
        daysRecyclerView.setHasFixedSize(true);
        daysRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        daysRecyclerView.setAdapter(new ProgramDetailsExercisesViewAdapter(currentProgramDay.getExercises(), context));
    }


    @Override
    public int getItemCount() {
        return days.size();
    }

}
