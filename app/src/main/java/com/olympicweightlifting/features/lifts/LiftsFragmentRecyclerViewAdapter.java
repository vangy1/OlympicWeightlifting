package com.olympicweightlifting.features.lifts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olympicweightlifting.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LiftsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<LiftsFragmentRecyclerViewAdapter.ViewHolder> {
    private List<LiftsContentData> liftsContentDataList;

    private Set<Integer> expandedCards = new HashSet<>();


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.expandable_group)
        LinearLayout expandableGroup;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.video_button)
        Button videoButton;

        ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public LiftsFragmentRecyclerViewAdapter(List<LiftsContentData> liftsContentDataList) {
        this.liftsContentDataList = liftsContentDataList;
    }

    @Override
    public LiftsFragmentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_lifts_fragment, parent, false);
        return new LiftsFragmentRecyclerViewAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(LiftsFragmentRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        LiftsContentData liftsContentData = liftsContentDataList.get(position);
        String videoUrl = liftsContentData.getVideoUrl();

        populateViewHolder(viewHolder, liftsContentData, videoUrl);
        handleCardExpansion(viewHolder, position);
    }

    private void populateViewHolder(ViewHolder viewHolder, LiftsContentData liftsContentData, String videoUrl) {
        viewHolder.title.setText(liftsContentData.getTitle());
        viewHolder.description.setText(liftsContentData.getDescription());
        if (videoUrl != null) {
            viewHolder.videoButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                view.getContext().startActivity(intent);
            });
        } else {
            viewHolder.expandableGroup.removeView(viewHolder.videoButton);
        }
    }

    private void handleCardExpansion(ViewHolder viewHolder, int position) {
        if (expandedCards.contains(position)) {
            expandCard(viewHolder);
        } else {
            collapseCard(viewHolder);
        }

        viewHolder.itemView.setOnClickListener(view -> {
            if (cardIsCollapsed(viewHolder)) {
                expandCard(viewHolder);
                expandedCards.add(position);
                notifyItemChanged(viewHolder.getAdapterPosition());

            } else if (cardIsExpanded(viewHolder)) {
                collapseCard(viewHolder);
                expandedCards.remove(position);
                notifyItemChanged(viewHolder.getAdapterPosition());

            }
        });
    }

    private void expandCard(ViewHolder viewHolder) {
        viewHolder.expandableGroup.setVisibility(View.VISIBLE);
    }

    private void collapseCard(ViewHolder viewHolder) {
        viewHolder.expandableGroup.setVisibility(View.GONE);
    }

    private boolean cardIsCollapsed(ViewHolder viewHolder) {
        return viewHolder.expandableGroup.getVisibility() == View.GONE;
    }

    private boolean cardIsExpanded(ViewHolder viewHolder) {
        return viewHolder.expandableGroup.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getItemCount() {
        return liftsContentDataList.size();
    }

}