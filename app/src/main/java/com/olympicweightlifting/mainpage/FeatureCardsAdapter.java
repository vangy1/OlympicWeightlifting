package com.olympicweightlifting.mainpage;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olympicweightlifting.R;

public class FeatureCardsAdapter extends RecyclerView.Adapter<FeatureCardsAdapter.ViewHolder> {
    private FeatureDataset[] featureDatasets;
    private Context context;
    private Typeface montserratTypeface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    public FeatureCardsAdapter(FeatureDataset[] featureDatasets, Context context) {
        this.featureDatasets = featureDatasets;
        this.context = context;
        montserratTypeface = Typeface.createFromAsset(context.getAssets(), "Montserrat-Bold.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_feature_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(cardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        FeatureDataset currentFeatureDataset = featureDatasets[position];

        TextView cardTitle = viewHolder.cardView.findViewById(R.id.feature_name);
        cardTitle.setTypeface(montserratTypeface);
        cardTitle.setText(currentFeatureDataset.getFeatureName());

        ImageView imageView = viewHolder.cardView.findViewById(R.id.feature_image);
        imageView.setImageResource(currentFeatureDataset.getFeatureImage());
        // set image

        LinearLayout linearLayout = viewHolder.cardView.findViewById(R.id.feature_shortcuts_layout);

        String[] featureShortcuts = currentFeatureDataset.getFeatureShortcuts();
        Button[] buttons = new Button[featureShortcuts.length];

        for(int i = 0; i < featureShortcuts.length; i++){
            Button featureShortcutButton = new Button(context);
//            int buttonHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, context.getResources().getDisplayMetrics());
            featureShortcutButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1));
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            featureShortcutButton.setBackgroundResource(outValue.resourceId);
            featureShortcutButton.setText(featureShortcuts[i]);
            buttons[i] = featureShortcutButton;
        }

        if(featureShortcuts.length == 2){
            View viewTop = new View(context);
            viewTop.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            View viewBottom = new View(context);
            viewBottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

//            linearLayout.addView(viewTop);
            for (Button button : buttons) {
                linearLayout.addView(button);
            }
//            linearLayout.addView(viewBottom);
        }
        if(featureShortcuts.length == 3){
            for (Button button : buttons) {
                linearLayout.addView(button);
            }
        }


    }

    @Override
    public int getItemCount() {
        return featureDatasets.length;
    }


}
