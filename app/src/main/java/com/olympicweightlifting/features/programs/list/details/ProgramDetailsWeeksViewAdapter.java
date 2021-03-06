package com.olympicweightlifting.features.programs.list.details;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicweightlifting.R;
import com.olympicweightlifting.features.programs.data.Program;
import com.olympicweightlifting.features.programs.data.ProgramWeek;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramDetailsWeeksViewAdapter extends RecyclerView.Adapter<ProgramDetailsWeeksViewAdapter.ViewHolder> {
    private final Context context;
    private List<ProgramWeek> weeks;

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title_week)
        TextView weekTitle;
        @BindView(R.id.recyclerview_days)
        RecyclerView daysRecyclerView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProgramDetailsWeeksViewAdapter(Program program, Context context) {
        this.weeks = program.getWeeks();
        this.context = context;
    }

    @Override
    public ProgramDetailsWeeksViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_programs_week, parent, false);
        return new ProgramDetailsWeeksViewAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ProgramDetailsWeeksViewAdapter.ViewHolder viewHolder, int position) {
        ProgramWeek currentProgramWeek = weeks.get(position);
        viewHolder.weekTitle.setText(String.format(context.getString(R.string.all_week) + " %s", String.valueOf(position + 1)));
        setupRecyclerView(viewHolder.daysRecyclerView, currentProgramWeek);
    }

    private void setupRecyclerView(RecyclerView daysRecyclerView, ProgramWeek currentProgramWeek) {
        daysRecyclerView.setHasFixedSize(true);
        daysRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        daysRecyclerView.setAdapter(new ProgramDetailsDaysViewAdapter(currentProgramWeek.getDays(), context));
    }


    @Override
    public int getItemCount() {
        return weeks.size();
    }
}
