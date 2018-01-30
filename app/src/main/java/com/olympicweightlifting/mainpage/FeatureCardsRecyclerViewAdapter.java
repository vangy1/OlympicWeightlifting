package com.olympicweightlifting.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olympicweightlifting.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeatureCardsRecyclerViewAdapter extends RecyclerView.Adapter<FeatureCardsRecyclerViewAdapter.ViewHolder> {
    private FeatureDataset[] featureDatasets;
    private Context activityContext;
    private Typeface montserratBoldTypeface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feature_name)
        TextView featureName;
        @BindView(R.id.feature_image)
        ImageView featureImage;
        @BindView(R.id.feature_shortcuts_layout)
        LinearLayout buttonsLayout;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public FeatureCardsRecyclerViewAdapter(FeatureDataset[] featureDatasets, Context activityContext) {
        this.featureDatasets = featureDatasets;
        this.activityContext = activityContext;
        montserratBoldTypeface = Typeface.createFromAsset(activityContext.getAssets(), activityContext.getString(R.string.font_path_montserrat_bold));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_feature_card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        FeatureDataset currentFeatureDataset = featureDatasets[position];

        viewHolder.featureName.setTypeface(montserratBoldTypeface);
        viewHolder.featureName.setText(currentFeatureDataset.getFeatureName());
        viewHolder.featureImage.setImageResource(currentFeatureDataset.getFeatureImage());

        String[] featureShortcuts = currentFeatureDataset.getFeatureShortcuts();
        Button[] buttons = new Button[featureShortcuts.length];

        createButtonsForShortcuts(featureShortcuts, buttons, currentFeatureDataset.getActivityToStart());
        addAllShortcutButtonsToLayout(viewHolder, buttons);
    }

    @Override
    public int getItemCount() {
        return featureDatasets.length;
    }

    private void createButtonsForShortcuts(String[] featureShortcuts, Button[] buttons, final Intent intent) {
        for (int i = 0; i < featureShortcuts.length; i++) {
            Button featureShortcutButton = new Button(activityContext);
            featureShortcutButton.setOnClickListener(view -> activityContext.startActivity(intent));

            featureShortcutButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            setRippleEffectToCoverEntireButtonArea(featureShortcutButton);
            featureShortcutButton.setText(featureShortcuts[i]);

            buttons[i] = featureShortcutButton;
        }
    }

    private void setRippleEffectToCoverEntireButtonArea(Button featureShortcutButton) {
        TypedValue outValue = new TypedValue();
        activityContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        featureShortcutButton.setBackgroundResource(outValue.resourceId);
    }

    private void addAllShortcutButtonsToLayout(ViewHolder viewHolder, Button[] buttons) {
        for (Button button : buttons) {
            viewHolder.buttonsLayout.addView(button);
        }
    }
}
